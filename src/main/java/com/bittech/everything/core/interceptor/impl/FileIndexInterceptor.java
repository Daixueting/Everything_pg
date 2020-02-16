package com.bittech.everything.core.interceptor.impl;

import com.bittech.everything.core.common.FileConverThing;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.interceptor.FileInterceptor;
import com.bittech.everything.core.model.Thing;

import java.io.File;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.interceptor.impl
 * @NAME: FileIndexInterceptor
 * @USER: 代学婷
 * @DESCRIPTION:将浏览的文件进行转换和写入数据库
 * @DATE: 2020/1/2
 **/
public class FileIndexInterceptor implements FileInterceptor {
    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(File file) {
        Thing thing= FileConverThing.conver(file);
        fileIndexDao.insert(thing);//插入Thing对象
    }


}
