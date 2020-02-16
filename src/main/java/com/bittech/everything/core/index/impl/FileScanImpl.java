package com.bittech.everything.core.index.impl;

import com.bittech.everything.config.EverythingPgConfig;
import com.bittech.everything.core.dao.DataSourceFactory;
import com.bittech.everything.core.dao.impl.FileIndexDaoImpl;
import com.bittech.everything.core.index.FileScan;
import com.bittech.everything.core.interceptor.FileInterceptor;
import com.bittech.everything.core.interceptor.impl.FileIndexInterceptor;
import com.bittech.everything.core.interceptor.impl.FilePrintInterceptor;

import java.io.File;
import java.util.LinkedList;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.index.impl
 * @NAME: FileIndexImpl
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2019/12/28
 **/
public class FileScanImpl implements FileScan {
    private EverythingPgConfig config=EverythingPgConfig.getInstance();
    private LinkedList<FileInterceptor> interceptors=new LinkedList<>();//功能转换器

    @Override
    public void index(String path) {//浏览完成一个盘符，进行转换，然后写入
        File file=new File(path);

        if (file.isFile()) {
            //D:\a\b\abc.pdf  ->  D:\a\b
            if (config.getExcludePath().contains(file.getParent())) {
                return;
            }
        } else {
            if (config.getExcludePath().contains(path)) {
                return;
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        index(f.getAbsolutePath());
                    }
                }
            }
        }
        //File  Directory
        for (FileInterceptor interceptor:this.interceptors){
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {  //添加拦截器

        this.interceptors.add(interceptor);
    }


}
