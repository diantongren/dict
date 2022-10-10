package com.diantongren.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dictionary {
    private Long id;

    private String name;

    private String dicCode;

    private Boolean status;

    private String remark;

    private Long createAt;

    private Long updateAt;

    private Boolean deleted;
}