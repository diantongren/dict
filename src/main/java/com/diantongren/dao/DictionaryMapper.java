package com.diantongren.dao;

import com.diantongren.model.Dictionary;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DictionaryMapper {
    int insert(Dictionary record);

    int batchInsert(@Param("list") List<Dictionary> list);
}