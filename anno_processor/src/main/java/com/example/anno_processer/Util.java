package com.example.anno_processer;

import java.awt.TextArea;

/**
 * @Author jacky.peng
 * @Date 2021/5/18 2:33 PM
 * @Version 1.0
 */
public class Util {
    //解析路由路径
    public static String parseGroup(String path) {
        if (path == null) throw new IllegalArgumentException("path can not be null");
        if (!path.startsWith("/")) throw new IllegalArgumentException("path can not be null");
        String group = path.substring(1, path.lastIndexOf("/"));
        if (group == null || group.isEmpty() || group.contains("/")) {
            throw new IllegalArgumentException("path resolve failed");
        }
        return group;
    }
}
