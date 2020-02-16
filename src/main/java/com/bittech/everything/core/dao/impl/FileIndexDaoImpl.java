package com.bittech.everything.core.dao.impl;

import com.bittech.everything.core.dao.DataSourceFactory;
import com.bittech.everything.core.dao.FileIndexDao;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.FileType;
import com.bittech.everything.core.model.Thing;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.dao.impl
 * @NAME: FileIndexDaoImpl
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2019/12/27
 **/
public class FileIndexDaoImpl implements FileIndexDao {
    private final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {  //数据库中存入的是Thing对象
        Connection connection=null;
        PreparedStatement statement=null;
        try {
            //1.获取数据库连接
            connection=dataSource.getConnection();
            //2.准备SQL语句
            String sql="insert into file_index(name,path,depth,file_type) values(?,?,?,?)";
            //3.准备命令
             statement=connection.prepareStatement(sql);
            //4.设置参数1 2 3 4
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            statement.setString(4,thing.getFileType().name());
            //5.执行命令
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }

    }

    @Override
    public List<Thing> search(Condition condition) {
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        List<Thing> things=new ArrayList<>();
        try {
            //1.获取数据库连接
            connection=dataSource.getConnection();
            //2.准备SQL语句
            //name ：like
            //fileType: =
            //limit:limit offset
            //orderbyAsc: order by
            StringBuilder sqlBuilder=new StringBuilder();//线程安全
            sqlBuilder.append(" select name, path, depth, file_type from file_index ");
            //name匹配：前模糊，后模糊，前后模糊
            sqlBuilder.append(" where ")
                    .append(" name like '%")
                    .append(condition.getName())
                    .append("%' ");
            if (condition.getFileType() != null) {
                sqlBuilder.append(" and file_type = '")
                        .append(condition.getFileType().toUpperCase())
                        .append("' ");
            }
            //limit, order必选的
            if (condition.getOrderByAsc() != null) {

                sqlBuilder.append(" order by depth ")
                        .append(condition.getOrderByAsc() ? "asc" : "desc");
            }
            if (condition.getLimit() != null) {
                sqlBuilder.append(" limit ")
                        .append(condition.getLimit())
                        .append(" offset 0 ");
            }
            //3.准备命令
            statement=connection.prepareStatement(sqlBuilder.toString());
            //5.执行命令
            resultSet=statement.executeQuery();
            //6.处理结果
            while (resultSet.next()){
                //数据库中的行记录--->Java中的Thing对象
                Thing thing=new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                String fileType=resultSet.getString("file_type");
                thing.setFileType(FileType.lookupByName(fileType)); //数据库中存入的枚举类对象的名称
                things.add(thing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
          releaseResource(resultSet,statement,connection);
        }
        return things;
    }

    @Override
    public void delete(Thing thing) {
        Connection connection=null;
        PreparedStatement statement=null;
        try {
            //1.获取数据库连接
            connection=dataSource.getConnection();
            //2.准备SQL语句
            String sql="delete from file_index where path like '" + thing.getPath() + "%'";
            //3.准备命令
            statement=connection.prepareStatement(sql);
            //4.设置参数1 2 3 4
            //5.执行命令
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }
    }





    private void releaseResource(ResultSet resultSet, Statement statement,Connection connection){
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
