package com.diantongren.service.impl;

import com.diantongren.dao.DictionaryValueMapper;
import com.diantongren.model.DictionaryValue;
import com.diantongren.service.DictionaryCacheService;
import com.diantongren.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DictionaryCacheServiceImpl implements DictionaryCacheService {
    final DictionaryValueMapper dictionaryValueMapper;
    final RedisUtil redisUtil;

    @Override
    @Cacheable(value = "dictValueMap", key = "#dictCode")
    public Map<String, String> getDictValueMapByDictCode(String dictCode) throws Exception {
        log.debug("从数据库查询, key: {}", dictCode);
        List<DictionaryValue> dictionaryValueList = dictionaryValueMapper.findByDictionaryCode(dictCode);
        if (CollectionUtils.isEmpty(dictionaryValueList)) {
            throw new Exception("数据字典不存在");
        }
        return dictionaryValueList.stream()
                .collect(Collectors.toMap(DictionaryValue::getValue, DictionaryValue::getLabel, (o, n) -> n));
    }

    @Override
    public Map<String, String> getDictValueMapByDictCodeWithRedis(String dictCode) throws Exception {
        Map<String, String> dictValueMap = redisUtil.getHashEntries(dictCode);
        if (CollectionUtils.isEmpty(dictValueMap)) {
            dictValueMap = getDictValueMapByDictCode(dictCode);
            redisUtil.add(dictCode, dictValueMap);
            return dictValueMap;
        }
        log.debug("从redis中查询, key: {}", dictCode);
        return dictValueMap;
    }

    @Override
    @CacheEvict(value = "dictValueMap", key = "#dictCode")
    public void removeDictCache(String dictCode) {
        log.debug("删除字典缓存, key: {}", dictCode);
    }

    @Override
    public void removeDictCacheInRedis(String dictCode) {
        log.debug("删除字典缓存, key: {}", dictCode);
        Set<String> keys = redisUtil.hashKeys(dictCode);
        String[] temp = new String[keys.size()];
        keys.toArray(temp);
        redisUtil.delete(dictCode, temp);
    }
}
