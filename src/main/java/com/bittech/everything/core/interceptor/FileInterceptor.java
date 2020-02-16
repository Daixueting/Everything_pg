package com.bittech.everything.core.interceptor;

import java.io.File;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.interceptor
 * @NAME: FileInterceptor
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/1/2
 **/
@FunctionalInterface
public interface FileInterceptor {
    void apply(File file);
}
