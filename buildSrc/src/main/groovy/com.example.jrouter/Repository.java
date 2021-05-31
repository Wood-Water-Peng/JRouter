package com.example.jrouter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/5/30 1:51 PM
 * @Version 1.0
 */
public class Repository {

    public static final String ROUTER_HELPER_CLASS = "com/example/jrouterapi/core/JRouteHelper.class";

    //每个模块中路由辅助类的接口实现
    public static final String ROUTER_MODULE_INTERFACE = "com/example/jrouterapi/IRouteModule";

    //要被注入字节码的class文件的路径
    public static InjectBean injectBean;
    //apt生成的辅助类的class文件路径
    private static List<String> classPathList = new ArrayList<>();

    public static void addClassPath(String path) {
        if (!classPathList.contains(path)) {
            classPathList.add(path);
        }
    }

    public static List<String> getClassPathList() {
        return classPathList;
    }

    public static void printClassPath() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < classPathList.size(); i++) {
            sb.append(classPathList.get(i));
            sb.append(",");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    public static class InjectBean {
        //注入的class文件路径
        public String injectClassName=ROUTER_HELPER_CLASS;
        //要注入的class文件所在的目录
        public String srcFilePath;
        //要注入的class文件的输出目录
        public String desFilePath;

        public InjectBean(String srcFilePath, String desFilePath) {
            this.srcFilePath = srcFilePath;
            this.desFilePath = desFilePath;
        }
    }
}
