package com.example.jrouterapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
    public void navigate(Context context){
        Intent intent = new Intent(context, targetClass);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
