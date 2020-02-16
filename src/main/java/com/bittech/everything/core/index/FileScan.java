package com.bittech.everything.core.index;

import com.bittech.everything.core.interceptor.FileInterceptor;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.index
 * @NAME: FileIndex
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2019/12/28
 **/
public interface FileScan {

    void index(String path); //给个路径然后去遍历它
    void interceptor(FileInterceptor interceptor);//遍历的拦截器
}
