package com.bittech.everything.cmd;

import com.bittech.everything.config.EverythingPgConfig;
import com.bittech.everything.core.EverythingPgManager;
import com.bittech.everything.core.model.Condition;
import com.bittech.everything.core.model.Thing;

import java.util.List;
import java.util.Scanner;

/**
 * @PACKAGE_NAME: com.bittech.evething.cmd
 * @NAME: EvethingPgCmdApp
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2019/12/23
 **/
public class EverythingPgCmdApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        //解析参数
       parseParams(args); //使用jar包的时候,或者配置args的时候

        //欢迎
        welcome();

        //统一调度器
        EverythingPgManager manager = EverythingPgManager.getInstance();

       //启动清理线程
        manager.starthingClearInterceptorThread();

        //启动监控
        manager.startFileSystemMonitor();

        //交互式
       interactive(manager);

    }

    private static void parseParams(String[] args) {
        EverythingPgConfig config = EverythingPgConfig.getInstance();
        for (String param :args){
            String maxReturnParam="--maxReturn=";
            if(param.startsWith(maxReturnParam)){
             int index=param.indexOf("=");
                String maxReturnStr = param.substring(index + 1);
                try {
                    int maxReturn = Integer.parseInt(maxReturnStr);
                    config.setMaxReturn(maxReturn);
                } catch (NumberFormatException e) {
                    //如果用户指定的参数格式不对，使用默认值即可
                }
            }
            String deptOrderByAscParam="--deptOrderByAsc=";
            if(param.startsWith(deptOrderByAscParam)){
                int index=param.indexOf("=");
                String deptOrderByAsc = param.substring(index + 1);
               config.setDeptOrderAsc(Boolean.parseBoolean(deptOrderByAsc)); //默认返回false
            }
            String includePathParam = "--includePath=";
            if (param.startsWith(includePathParam)) {
                //--includePath=values (;)
                int index = param.indexOf("=");
                String includePathStr = param.substring(index + 1);
                String[] includePaths = includePathStr.split(";");
                if (includePaths.length > 0) {
                    config.getIncludePath().clear();
                }
                for (String p : includePaths) {
                    config.getIncludePath().add(p);
                }
            }
            String excludePathParam= "--excludePath=";
            if (param.startsWith(excludePathParam)) {
                //--excludePath=values (;)
                int index = param.indexOf("=");
                String excludePathStr = param.substring(index + 1);
                String[] excludePaths = excludePathStr.split(";");
                if (excludePaths.length > 0) {
                    config.getExcludePath().clear();
                }
                for (String p : excludePaths) {
                    config.getExcludePath().add(p);
                }

            }
        }
    }


    private static void interactive(EverythingPgManager manager) {
        while (true) {
            System.out.print("everything >>");
            String input = scanner.nextLine();
            //优先处理search
            if (input.startsWith("search")) {
                //search name [file_type]
                String[] values = input.split(" ");
                if (values.length >= 2) {
                    if (!values[0].equals("search")) {
                        help();
                        continue;
                    }
                    Condition condition = new Condition();
                    String name = values[1];
                    condition.setName(name);
                    if (values.length >= 3) {
                        String fileType = values[2];
                        condition.setFileType(fileType.toUpperCase());
                    }
                    search(manager, condition);
                    continue;
                } else {
                    help();
                    continue;
                }
            }
            switch (input) {
                case "help":
                    help();
                    break;
                case "quit":
                    quit();
                    return;
                case "index":
                    index(manager);
                    break;
                default:
                    help();
            }
        }
    }

    private static void search(EverythingPgManager manager, Condition condition) {
        System.out.println("检索功能");
        //统一调度器中的search
        //name fileType limit orderByAsc
        condition.setLimit(EverythingPgConfig.getInstance().getMaxReturn());
        condition.setOrderByAsc(EverythingPgConfig.getInstance().getDeptOrderAsc());
        List<Thing> thingList=manager.search(condition);
        for (Thing thing:thingList)
            System.out.println(thing.getPath());
    }

    private static void index(EverythingPgManager manager) {
        //统一调度器中的index
        new Thread(new Runnable() {
            @Override
            public void run() {
                manager.buildIndex();
            }
        }).start();
        //hemanager.buildIndex();
    }

    private static void quit() {
        System.out.println("再见");
        System.exit(0);
    }

    private static void welcome() {
        System.out.println("欢迎使用:Everything_pg");
    }

    private static void help() {
        System.out.println("命令列表：");
        System.out.println("退出：quit");
        System.out.println("帮助：help");
        System.out.println("索引：index");
        System.out.println("搜索：search <name> [<file-Type> img | doc | bin | archive | other]");
    }
}
