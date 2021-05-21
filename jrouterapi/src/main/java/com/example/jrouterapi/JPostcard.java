package com.example.jrouterapi;

/**
 * @Author jacky.peng
 * @Date 2021/5/18 10:03 AM
 * @Version 1.0
 * 封装路由信息
 */
public class JPostcard {
    String group;
    String path;
    Class<?> targetClass;

    public JPostcard(String group, String path, Class<?> targetClass) {
        this.group = group;
        this.path = path;
        this.targetClass = targetClass;
    }
}
