package com.bittech.everything.core.model;

/**
 * @PACKAGE_NAME: com.bittech.everthing.core.model
 * @NAME: FileType
 * @USER: 代学婷
 * @DESCRIPTION: 文件类型,给一个文件类型，来获取扩展名
 * @DATE: 2019/12/23
 **/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum FileType {
    IMG("png","jpeg","jpe","gif"),
    DOC("ppt","pptx","doc","docx","pdf"),
    BIN("exe","sh","jar","msi"),
    ARCHIVE("zip","rar"),
    OTHER("*");

    //对应文件类型的扩展名集合
    private Set<String> extend=new HashSet<>();
    private  FileType(String...extend){
        this.extend.addAll(Arrays.asList(extend));
    }


    public static FileType lookup(String enxtend){
        for (FileType fileType: FileType.values()
             ) {
           // System.out.println(fileType);
           if (fileType.extend.contains(enxtend)){
                return fileType;
            }
        }
        return OTHER;
    }

   //根据文件类型的名字（String）获取一个文件类型的对象
  public static FileType lookupByName(String name){
      for (FileType fileType: FileType.values()) {
          if (fileType.name().equals(name)){
              return fileType;
          }
      }
      return OTHER;
  }

}
