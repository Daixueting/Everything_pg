package com.bittech.everything.core.search.impl;

import com.bittech.everything.core.dao.DataSourceFactory;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.dao.impl.FileIndexDaoImpl;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;
import com.bittech.everything.core.search.FileSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.search.impl
 * @NAME: FileSearchImpl
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2019/12/27
 **/
public class FileSearchImpl implements FileSearch {

    private final FileIndexDao fileIndexDao;

    public FileSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }


    @Override
    public List<Thing> search(Condition condition) {
        if (condition==null){
            return new ArrayList<>();
        }
        return fileIndexDao.search(condition);
    }


}
