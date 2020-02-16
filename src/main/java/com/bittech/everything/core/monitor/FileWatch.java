package com.bittech.everything.core.monitor;

import com.bittech.everything.core.common.HandlePath;

/**
 * @PACKAGE_NAME: com.bittech.everything.core.monitor
 * @NAME: FileWatch
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/1/11
 **/
public interface FileWatch {
    void start();

    void monitor(HandlePath handlePath);  //监听的目录

    void stop();
}
