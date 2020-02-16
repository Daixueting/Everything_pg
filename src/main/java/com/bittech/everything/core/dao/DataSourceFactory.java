package com.bittech.everything.core.dao;


import com.alibaba.druid.pool.DruidDataSource;
import com.bittech.everything.config.EverythingPgConfig ;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.dao
 * @NAME: DataSourceFactory
 * @USER: 代学婷
 * @DESCRIPTION:利用单例和工厂模式实现数据源实现和创建的解耦
 * @DATE: 2019/12/24
 **/
public class DataSourceFactory {
     private static  volatile DruidDataSource dataSource; //数据源

     private DataSourceFactory(){//单例：构造方法私有化

     }
     //创建一个唯一的数据源
     public static DataSource dataSource(){
          if (dataSource==null){
             synchronized (DataSourceFactory.class){  //抢锁是一个过程
                 if (dataSource==null){
                     dataSource=new DruidDataSource();   //创建一个数据源,但是没有跟数据库连接起来

                     dataSource.setDriverClassName("org.h2.Driver");  //与h2数据库取得联系
                     //url,username,password,
                     //H2是嵌入式数据库，数据库以本地文件的方式存储，只需要提供url接口就可以

                     //相当于创建了一个everthing-pg的数据库
                     dataSource.setUrl("jdbc:h2:"+ EverythingPgConfig.getInstance().getH2IndexPath());
                     //设置链接池不工作默认的关闭时间
                     dataSource.setTestWhileIdle(false); //关闭验证
                    // dataSource.setValidationQuery("SELECT 1"); //发送验证语句
                 }
             }
         }
          return dataSource;
     }

      //初始化数据库---若是表存在则删除后重新建立
     public static void initDatabase(){
         //1.获取数据源
         //2.获取sql语句
         //不采取读绝对路径，采取读classpath路径下的文件
         InputStream in= DataSourceFactory.class.getClassLoader().getResourceAsStream("everything_pg.sql");
          if (in==null){
              throw new RuntimeException("Not read init database script please check it");
          }
           BufferedReader reader=new BufferedReader(new InputStreamReader(in));
           StringBuilder sqlBuilder=new StringBuilder();
           String line=null;
          try {
              while (((line=reader.readLine())!=null)){
                  if (!line.startsWith("--")){
                      sqlBuilder.append(line);
                  }
              }
              in.close();
              reader.close();
              //3.获取数据库连接和名称执行SQL
              String sql=sqlBuilder.toString();
              //3.1获取数据库连接
              Connection connection=dataSource.getConnection();
              //3.2创建命令
              PreparedStatement statement=connection.prepareStatement(sql);
              //3.3执行SQL命令
              statement.execute();

              connection.close();
              statement.close();

          } catch (IOException e) {
              e.printStackTrace();
          } catch (SQLException e) {
              e.printStackTrace();
          }
     }

}
