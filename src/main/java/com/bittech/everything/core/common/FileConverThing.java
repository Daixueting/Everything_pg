package com.bittech.everything.core.common;

import com.bittech.everything.core.model.FileType;
import com.bittech.everything.core.model.Thing;

import java.io.File;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.common
 * @NAME: FileConverThing
 * @USER: 代学婷
 * @DESCRIPTION:辅助工具类，将File对象转换成Thing对象(不能被继承)
 * @DATE: 2019/12/28
 **/
public final class FileConverThing {

   private FileConverThing() {
    }

    public static Thing conver(File file){
        Thing thing=new Thing();
        thing.setName(file.getName());
        thing.setPath(file.getAbsolutePath());
        thing.setDepth(computeFileDepth(file));
        thing.setFileType(computeFileType(file));
        return thing;
    }

    private  static int computeFileDepth(File file){
        String[] segments=file.getAbsolutePath().split("\\\\");
        return  segments.length;
    }

    private static FileType computeFileType(File file){//如说是文件夹则返回FileType.other
        if (file.isDirectory()){
            return FileType.OTHER;
        }
        int index=file.getName().lastIndexOf(".");
        if (index!=-1&&index<file.getName().length()){//防止abc. 这种情况

            String extend=file.getName().substring(index+1);
            return FileType.lookup(extend);
        }
        return FileType.OTHER;
    }

}
