package com.diantongren.service;

import com.diantongren.utils.Page;
import com.diantongren.vo.UserVo;

import java.util.List;

public interface UserService {
    UserVo findById(Long id);

    List<UserVo> getList();

    Page<UserVo> getPage();
}
