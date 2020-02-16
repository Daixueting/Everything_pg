package com.bittech.everything.core.monitor.impl;

import com.bittech.everything.core.common.FileConverThing;
import com.bittech.everything.core.common.HandlePath;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.monitor.FileWatch;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;

/**
 * @PACKAGE_NAME: com.bittech.everything.core.monitor.impl
 * @NAME: FileWatchImpl
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/1/11
 **/
public class FileWatchImpl implements FileWatch, FileAlterationListener {
    private FileIndexDao fileIndexDao;
    private FileAlterationMonitor monitor; //用来启动监听的

    public FileWatchImpl (FileIndexDao fileIndexDao){
        this.fileIndexDao=fileIndexDao;
        this.monitor=new FileAlterationMonitor(10);//传入监听时间是毫秒
    }
    @Override
    public void start() {
        try {
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void monitor(HandlePath handlePath) {
        for (String path : handlePath.getIncludePath()) {
            FileAlterationObserver observer = new FileAlterationObserver(
                    path, pathname -> {
                String currentPath = pathname.getAbsolutePath();
                for (String excludePath : handlePath.getExcludePath()) {
                    if (excludePath.startsWith(currentPath)) {
                        return false;
                    }
                }
                return true;
            });
            observer.addListener(this);
            this.monitor.addObserver(observer);
        }
    }

    @Override
    public void stop() {
        try {
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onStart(FileAlterationObserver observer){
        //observer.addListener(this);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate"+directory);
        this.fileIndexDao.insert(FileConverThing.conver(directory));
    }

    @Override
    public void onDirectoryChange(File directory) {

    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryDelete"+directory);
        this.fileIndexDao.insert(FileConverThing.conver(directory));
    }

    @Override
    public void onFileCreate(File file) {
     //文件创建---》存入到数据库当中
        System.out.println("onFileCreate"+file);
        this.fileIndexDao.insert(FileConverThing.conver(file));
    }

    @Override
    public void onFileChange(File file) {

    }

    @Override
    public void onFileDelete(File file) {
      //删除文件
        System.out.println("onFileDelete"+file);
        this.fileIndexDao.delete(FileConverThing.conver(file));
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
     //   observer.removeListener(this);
    }
}
