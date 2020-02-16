package com.bittech.everything.core.search;

import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;

import java.util.List;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.search
 * @NAME: FileSearch
 * @USER: 代学婷
 * @DESCRIPTION:根据condition条件进行数据库的检索
 * @DATE: 2019/12/27
 **/
public interface FileSearch {
    List<Thing> search(Condition condition);
}
