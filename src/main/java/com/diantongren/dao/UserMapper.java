package com.diantongren.dao;

import com.diantongren.model.User;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insert(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKey(User record);

    int batchInsert(@Param("list") List<User> list);

    List<User> findByIdIn(@Param("idCollection") Collection<Long> idCollection);
}