package com.bittech.everything.core.interceptor.impl;

import com.bittech.everything.core.dao.DataSourceFactory;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.interceptor.ThingInterceptor;
import com.bittech.everything.core.model.Thing;

import javax.sql.DataSource;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @PACKAGE_NAME: com.bittech.everything.core.interceptor.impl
 * @NAME: ThingClearInterceptor
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/1/10
 **/
public class ThingClearInterceptor implements ThingInterceptor ,Runnable{
    private FileIndexDao fileIndexDao;
    private Queue<Thing> queue=new ArrayBlockingQueue<>(1024);

    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(Thing thing) {
        this.queue.add(thing);
    }

    @Override
    public void run() {
        while (true){
            Thing thing=queue.poll();
            if (thing!=null){
                fileIndexDao.delete(thing);
            }
        }

    }
}
