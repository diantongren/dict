package com.diantongren.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diantongren.service.DictionaryCacheService;
import com.diantongren.utils.Page;
import com.diantongren.utils.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class DictConvertAspect {
    final DictionaryCacheService dictionaryCacheService;


    @Pointcut("@annotation(DictConvert)")
    public void dict() {

    }

    @Around("dict()")
    public <T> Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed();
        try {
            if (object instanceof List<?> && !CollectionUtils.isEmpty((List<?>) object)) {
                return listConvert(object);
            } else if (object instanceof Page<?> && ((Page<?>) object).getTotal() > 0) {
                Page<Object> page = (Page<Object>) object;
                List<Object> list = page.getList();
                page.setList((List<Object>) listConvert(list));
                return page;
            } else if (!StringUtils.isEmpty(object)) {
                return this.singleConvert(object);
            } else {
                return object;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return object;
        }
    }

    private <T> Object singleConvert(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);
        JSONObject jsonObject = JSONObject.parseObject(json);

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            DictObj dictObj = field.getAnnotation(DictObj.class);
            if (null != dictObj) {
                // 如果是内层需要转换字段，则继续递归判断
                String fieldName = field.getName();
                Object fieldObject = jsonObject.get(fieldName);

                Object convertObject = null;
                Object subObject = null;
                if (fieldObject instanceof List) {
                    // 获取该字段对应的实体类
                    subObject = this.getFieldObject(object, fieldName);
                    List<?> subList = (List<?>) subObject;
                    if (!CollectionUtils.isEmpty(subList)) {
                        convertObject = this.listConvert(subObject);
                    }
                } else {
                    // 获取该字段对应的实体类
                    subObject = this.getFieldObject(object, fieldName);
                    if (subObject != null) {
                        convertObject = this.singleConvert(subObject);
                    }

                }
                jsonObject.put(fieldName, convertObject);
            }
            Dict dict = field.getAnnotation(Dict.class);
            if (dict != null) {
                String fieldName = field.getName();
                jsonObject.put(fieldName, dictTranslate(dict, jsonObject, fieldName));
            }
        }
        return JSON.toJavaObject(jsonObject, clazz);
    }

    private <T> Object listConvert(Object object) throws Exception {

        List<?> items = (List<?>) object;
        Object item;
        List<T> newList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            item = singleConvert(item);
            newList.add(i, (T) item);
        }
        return newList;
    }


    /**
     * 获取报文字段对应的实体类
     */
    private Object getFieldObject(Object o, String fieldName)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object subObject = null;
        Method[] m = o.getClass().getMethods();
        for (Method method : m) {
            if (("get" + fieldName).equalsIgnoreCase(method.getName())) {
                subObject = method.invoke(o);
            }
        }
        return subObject;
    }

    // 使用spring 缓存
    private Object dictTranslate(Dict dict, JSONObject jsonObject, String fieldName) throws Exception {
        String dictCode = dict.dictCode();
        // 使用spring缓存或者redis
//        Map<String, String> dictValueMap = dictionaryCacheService.getDictValueMapByDictCode(dictCode);
        Map<String, String> dictValueMap = dictionaryCacheService.getDictValueMapByDictCodeWithRedis(dictCode);
        Object fieldObject = jsonObject.get(fieldName);

        if (dictValueMap == null) {
            return fieldObject;
        }

        if (fieldObject instanceof List) {
            return ((List<?>) fieldObject).stream()
                    .map(Object::toString)
                    .map(dictValueMap::get)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            String key = String.valueOf(fieldObject);
            return dictValueMap.get(key);
        }
    }
}
