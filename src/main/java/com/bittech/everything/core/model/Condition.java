package com.bittech.everything.core.model;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.model
 * @NAME: Condition
 * @USER: 代学婷
 * @DESCRIPTION:检索的参数（条件）
 * @DATE: 2019/12/23
 **/
@Data
public class Condition {
    private String name;

    private String fileType;

    private Integer limit;

    private Boolean orderByAsc;//默认检索结果的文件信息depth排序规则，默认true->asc,false->desc


}
