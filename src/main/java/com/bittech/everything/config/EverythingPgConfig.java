package com.bittech.everything.config;


import lombok.Getter;
import lombok.Setter;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.io.File;


/**
 * @PACKAGE_NAME: com.bittech.everthing.config
 * @NAME: EverthingPgConfig
 * @USER: 代学婷
 * @DESCRIPTION:配置类
 * @DATE: 2019/12/28
 **/
@Getter
public class EverythingPgConfig {

    private static volatile EverythingPgConfig config;

    private Set<String> includePath=new HashSet<>();//包含的路径
    private Set<String> excludePath=new HashSet<>();//排除的路径
    @Setter
    private Integer maxReturn=30;
    @Setter
    private Boolean deptOrderAsc=true;

    //H2数据库的文件路径
    private String h2IndexPath=System.getProperty("user.dir")+File.separator+"everthing_pg";


     private EverythingPgConfig(){

     }

    private void initDefaultPathConfig(){ //初始化默认的配置
        //遍历的目录
        FileSystem fileSystem=FileSystems.getDefault();
        Iterable<Path> iterable=fileSystem.getRootDirectories();
        iterable.forEach(path -> config.includePath.add(path.toString()));
        //排除的目录
        //Windows:      C:\Windows      C:\Program Files (x86)    C:\Program Files FileSystem fileSystem = FileSystems.getDefault();//获取计算机中文件的系统
        //Linux : /tmp /etc
        String osname = System.getProperty("os.name");//获取当前的系统名称
        if (osname.startsWith("Windows")) {
            config.getExcludePath().add("C:\\Windows");
            config.getExcludePath().add("C:\\Program Files (x86) ");
            config.getExcludePath().add("C:\\Program Files ");
            config.getExcludePath().add("C:\\ProgramData ");
        } else {
            config.getExcludePath().add("/tmp");
            config.getExcludePath().add("/etc");
        }
    }



    public static EverythingPgConfig getInstance(){
        if (config==null){
            synchronized (EverythingPgConfig.class){
                if (config==null){

                    config=new EverythingPgConfig();
                    config.initDefaultPathConfig();

                }
            }
        }
        return config;
    }

    @Override
    public String toString() {
        return "EverythingPgConfig{" +
                "includePath=" + includePath +
                ", excludePath=" + excludePath +
                ", maxReturn=" + maxReturn +
                ", deptOrderAsc=" + deptOrderAsc +
                ", h2IndexPath='" + h2IndexPath + '\'' +
                '}';
    }

}
