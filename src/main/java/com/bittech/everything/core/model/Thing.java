package com.bittech.everything.core.model;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.model
 * @NAME: Thing
 * @USER: 代学婷
 * @DESCRIPTION:文件属性信息索引之后的记录(存入数据库)，Thing表示
 * @DATE: 2019/12/23
 **/
@Data  //Lombok插件，getter,setter,toString生成完成
public class Thing {

    private String name;//文件名称（只保存文件的名称）

    private String path;//文件路径

    private Integer depth;//文件深度

    private FileType fileType; //文件扩展名


}
