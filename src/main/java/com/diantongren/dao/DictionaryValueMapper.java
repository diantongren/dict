package com.diantongren.dao;

import com.diantongren.model.DictionaryValue;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DictionaryValueMapper {
    int insert(DictionaryValue record);

    int batchInsert(@Param("list") List<DictionaryValue> list);

    List<DictionaryValue> findByDictionaryCode(@Param("dictCode") String dictCode);
}