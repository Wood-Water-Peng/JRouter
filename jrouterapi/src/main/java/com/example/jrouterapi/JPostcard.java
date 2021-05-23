package com.example.jrouterapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.jrouterapi.interceptor.IRouteInterceptor;
import com.example.jrouterapi.interceptor.JRouteInterceptorChain;
import com.example.jrouterapi.interceptor.LastInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/5/18 10:03 AM
 * @Version 1.0
 * 封装路由信息,一次跳转对应该类的一个实例
 */
public class JPostcard {
    String group;
    String path;
    Class<?> targetClass;
    private Bundle params;

    public String getGroup() {
        return group;
    }

    public String getPath() {
        return path;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Bundle getParams() {
        return params;
    }

    public JPostcard(String group, String path, Class<?> targetClass) {
        this.group = group;
        this.path = path;
        this.targetClass = targetClass;
    }

    public void navigate(Context context) {
        //生一个一个拦截器责任链条
        List<IRouteInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LastInterceptor());
        new JRouteInterceptorChain(interceptors, new IRouteInterceptor.Callback() {
            @Override
            public void onSuccess(@NonNull JPostcard jPostcard) {
                _navigate(context);
            }

            @Override
            public void onFail(@NonNull Throwable exception) {

            }
        }, 0).proceed(this);
    }

    public JPostcard withParam(Bundle bundle) {
        if (params == null) {
            params = bundle;
        } else {
            params.putAll(bundle);
        }
        return this;
    }

    private void _navigate(Context context) {
        Intent intent = new Intent(context, targetClass);
        if (params != null) {
            intent.putExtras(params);
        }
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
