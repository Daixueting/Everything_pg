package com.bittech.everything.core.dao;

import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;

import java.util.List;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.dao
 * @NAME: FileIndexDao
 * @USER: 代学婷
 * @DESCRIPTION:关于业务层访问数据库的CRUD
 * @DATE: 2019/12/27
 **/
public interface FileIndexDao {
    void insert(Thing thing);                 //文件的索引

    List<Thing> search(Condition condition);  //文件检索

    void delete(Thing thing);                //删除文件thing
}
