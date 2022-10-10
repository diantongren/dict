package com.diantongren.service.impl;

import com.diantongren.dao.UserMapper;
import com.diantongren.model.User;
import com.diantongren.service.UserService;
import com.diantongren.utils.Page;
import com.diantongren.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserMapper userMapper;

    @Override
    public UserVo findById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setName(user.getName());
        userVo.setGender(user.getSex().toString());
        userVo.setHobby(user.getHobby().stream().map(Objects::toString).collect(Collectors.toList()));
        return userVo;
    }

    @Override
    public List<UserVo> getList() {
        return userMapper.findByIdIn(Arrays.asList(1L, 2L, 3L, 4L, 5L)).stream()
                .map(user -> {
                    UserVo userVo = new UserVo();
                    userVo.setId(user.getId());
                    userVo.setName(user.getName());
                    userVo.setGender(user.getSex().toString());
                    userVo.setHobby(user.getHobby().stream().map(Objects::toString).collect(Collectors.toList()));
                    return userVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserVo> getPage() {
        List<UserVo> collect = userMapper.findByIdIn(Arrays.asList(1L, 2L, 3L, 4L, 5L)).stream()
                .map(user -> {
                    UserVo userVo = new UserVo();
                    userVo.setId(user.getId());
                    userVo.setName(user.getName());
                    userVo.setGender(user.getSex().toString());
                    userVo.setHobby(user.getHobby().stream().map(Objects::toString).collect(Collectors.toList()));
                    return userVo;
                })
                .collect(Collectors.toList());
        return Page.create((long) collect.size(), collect);
    }

    private UserVo getChild() {
        return Optional.ofNullable(userMapper.selectByPrimaryKey(2L))
                .map(user -> {
                    UserVo userVo = new UserVo();
                    userVo.setId(user.getId());
                    userVo.setName(user.getName());
                    userVo.setGender(user.getSex().toString());
                    userVo.setHobby(Arrays.asList("1", "2"));
                    return userVo;
                })
                .orElse(null);
    }

    private List<UserVo> getChildren() {
        return userMapper.findByIdIn(Arrays.asList(3L, 4L, 5L)).stream()
                .map(item -> {
                    UserVo vo = new UserVo();
                    vo.setId(item.getId());
                    vo.setName(item.getName());
                    vo.setGender(item.getSex().toString());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
