package com.diantongren.vo;

import com.diantongren.aop.Dict;
import com.diantongren.aop.DictObj;
import lombok.Data;

import java.util.List;

@Data
public class UserVo {
    private Long id;
    private String name;
    @Dict(dictCode = "sex")
    private String gender;
    @Dict(dictCode = "hobby")
    private List<String> hobby;
}
