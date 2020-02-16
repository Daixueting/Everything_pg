package com.bittech.everything.core.interceptor.impl;

import com.bittech.everything.core.interceptor.FileInterceptor;

import java.io.File;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.interceptor.impl
 * @NAME: FilePrintInterceptor
 * @USER: 代学婷
 * @DESCRIPTION:将浏览饿文件进行打印
 * @DATE: 2020/1/2
 **/
public class FilePrintInterceptor implements FileInterceptor {

    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
