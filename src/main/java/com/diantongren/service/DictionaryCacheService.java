package com.diantongren.service;

import java.util.Map;

public interface DictionaryCacheService {

    /**
     * 根据字典code查询字典值并保存在缓存中
     *
     * @param dictCode 字典code
     * @return Map<String, String>
     */
    Map<String, String> getDictValueMapByDictCode(String dictCode) throws Exception;

    /**
     * 根据字典code查询字典值 使用redis
     *
     * @param dictCode 字典code
     * @return Map<String, String>
     */
    Map<String, String> getDictValueMapByDictCodeWithRedis(String dictCode) throws Exception;

    /**
     * 后台变更字典后，需要删除对应的缓存，下次再从数据库中读取
     * @param dictCode 字典code
     */
    void removeDictCache(String dictCode);

    /**
     * 后台变更字典后，需要删除对应的缓存，下次再从数据库中读取
     * @param dictCode 字典code
     */
    void removeDictCacheInRedis(String dictCode);
}
