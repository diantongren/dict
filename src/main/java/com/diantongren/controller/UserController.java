package com.diantongren.controller;

import com.diantongren.aop.DictConvert;
import com.diantongren.model.User;
import com.diantongren.service.UserService;
import com.diantongren.utils.Page;
import com.diantongren.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping("/{id}")
    @DictConvert
    public UserVo findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping("")
    @DictConvert
    public List<UserVo> getList() {
        return userService.getList();
    }

    @GetMapping("/page")
    @DictConvert
    public Page<UserVo> getPage() {
        return userService.getPage();
    }
}
