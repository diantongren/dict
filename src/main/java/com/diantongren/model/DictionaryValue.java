package com.diantongren.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryValue {
    private Long id;

    private String label;

    private String value;

    private Integer sort;

    private Boolean isDefault;

    private String cssClass;

    private String listClass;

    private Boolean status;

    private String remark;

    private Long pid;

    private Long dictionaryId;

    private Long createAt;

    private Long updateAt;

    private Boolean deleted;
}