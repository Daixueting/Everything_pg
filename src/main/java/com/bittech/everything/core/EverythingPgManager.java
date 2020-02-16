package com.bittech.everything.core;

import com.bittech.everything.config.EverythingPgConfig;
import com.bittech.everything.core.common.HandlePath;
import com.bittech.everything.core.dao.DataSourceFactory;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.dao.impl.FileIndexDaoImpl;
import com.bittech.everything.core.index.FileScan;
import com.bittech.everything.core.index.impl.FileScanImpl;
import com.bittech.everything.core.interceptor.impl.FileIndexInterceptor;
import com.bittech.everything.core.interceptor.impl.ThingClearInterceptor;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;
import com.bittech.everything.core.monitor.FileWatch;
import com.bittech.everything.core.monitor.impl.FileWatchImpl;
import com.bittech.everything.core.search.FileSearch;
import com.bittech.everything.core.search.impl.FileSearchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core
 * @NAME: EverthingPgManager
 * @USER: 代学婷
 * @DESCRIPTION:统一调度器(单利)：对主要代码进行扩展
 * @DATE: 2020/1/2
 **/
public class EverythingPgManager{
    private static volatile EverythingPgManager manager;

    private FileSearch fileSearch;

    private FileScan fileScan;

    private ExecutorService executorService;

    //文件删除
   private ThingClearInterceptor thingClearInterceptor;

   private Thread thingClearInterceptorThread;
   //表示守护线程的启动状态--》不能多次启动
   private AtomicBoolean thingClearInterceptorThreadStatus=new AtomicBoolean(false);

   //文件监控
    private FileWatch fileWatch;

   private EverythingPgManager() {
        this.initComponent();
    }



    private void initComponent() {
        //数据源对象
        DataSource dataSource = DataSourceFactory.dataSource();

        //数据库初始化
        initOrReseDatabase();

        //业务层的对象
        FileIndexDao fileIndexDao = new FileIndexDaoImpl(dataSource);

        this.fileSearch = new FileSearchImpl(fileIndexDao);

        this.fileScan = new FileScanImpl();

        //发布代码的时候不需要
        //this.fileScan.interceptor(new FilePrintInterceptor());

        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));

        this.thingClearInterceptor=new ThingClearInterceptor(fileIndexDao);


        this.thingClearInterceptorThread=new Thread(this.thingClearInterceptor);
        //将线程变成守护线程，如果没有任务要删除，线程就会关闭
        this.thingClearInterceptorThread.setName("Thread-Thing-Clear");
        this.thingClearInterceptorThread.setDaemon(true);

        //文件监控对象
        this.fileWatch=new FileWatchImpl(fileIndexDao);

    }

    private void initOrReseDatabase() {
       //数据库一但连接，文件必然存在
//        String fileName=EverythingPgConfig.getInstance().getH2IndexPath()+".mv.db";
//        File dbFile=new File(fileName);
//        if (dbFile.isFile()&&!dbFile.exists()){
//            DataSourceFactory.initDatabase();
//        }
        DataSourceFactory.initDatabase();
    }



    public static EverythingPgManager getInstance() {
        if (manager == null) {
            synchronized(EverythingPgManager.class) {
                if (manager == null) {
                    manager = new EverythingPgManager();  //FileScanImpl的对象要添加拦截器
                }
            }
        }
        return manager;
    }


    //检索功能
    public List<Thing>  search(Condition condition){
        //扩展点 流式处理---》若是电脑中删除可以文件，异步操作删除
        return this.fileSearch.search(condition).stream().filter(thing -> {
            String path=thing.getPath();
            File file=new File(path);
            boolean flag=file.exists();
            if (! flag){
                //做删除  --->异步操作删除（首先要加到要删除的队列中，在起动守护线程进行删除）
             this.thingClearInterceptor.apply(thing);
            }
            return flag;
        }).collect(Collectors.toList()); //true 加入集合
    }

    //索引
    public void buildIndex(){  //索引里面是的变量是线程安全的，如果你是线程安全的，那变量就是线程私有的
        initOrReseDatabase();//保证数据库中的表是空的
        Set<String> directories=EverythingPgConfig.getInstance().getIncludePath();

        if (this.executorService==null){
            this.executorService=Executors.newFixedThreadPool(directories.size(), new ThreadFactory() {
                private final AtomicInteger threadId=new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread=new Thread(r);
                    thread.setName("Thread-Scan-"+threadId.getAndIncrement());
                    return thread;
                }
            });
        }

        final CountDownLatch countDownLatch=new CountDownLatch(directories.size());


        System.out.println("Build index start ....");
        for (String path:directories){//放任务
           // this.fileScan.index(path);   普通遍历，没有调用多线程
           this.executorService.execute(() -> {
               EverythingPgManager.this.fileScan.index(path);
               countDownLatch.countDown();//每个线程完成任务则countDownLatch就会减一,直到会变为0
           });
        }

        try {
            countDownLatch.await();//阻塞，直到任务完成，值为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Build index complete ....");
    }


    //启动清理线程
    public void starthingClearInterceptorThread(){
       if (this.thingClearInterceptorThreadStatus.compareAndSet(false,true)){
       this.thingClearInterceptorThread.start();
       }else {
        System.out.println("线程不能多次启动");
    }

   }

   //启动文件系统监听
    public void startFileSystemMonitor(){
        EverythingPgConfig config=EverythingPgConfig.getInstance();
        HandlePath handlePath=new HandlePath();
        handlePath.setExcludePath(config.getExcludePath());
        handlePath.setIncludePath(config.getIncludePath());
        this.fileWatch.monitor(handlePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("文件系统监控启动");
                fileWatch.start(); //启动--》后台线程进行异步处理
            }
        }).start();

    }

}
