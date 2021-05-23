package com.example.jrouterapi.interceptor;

import androidx.annotation.NonNull;

import com.example.jrouterapi.JPostcard;

import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/5/23 5:14 PM
 * @Version 1.0
 */
public class JRouteInterceptorChain implements IRouteInterceptor.Chain {
    @NonNull
    private final List<IRouteInterceptor> interceptors;
    private final IRouteInterceptor.Callback callback;
    /**
     * 要执行的跳转对象
     */
    private JPostcard postcard;
    /**
     * 拦截器的下标
     */
    private int index;

    public JRouteInterceptorChain(@NonNull List<IRouteInterceptor> interceptors, IRouteInterceptor.Callback callback, int index) {
        this.interceptors = interceptors;
        this.callback = callback;
        this.index = index;
    }

    @Override
    public void proceed(@NonNull JPostcard jPostcard) {
        this.postcard = jPostcard;
        //取出所有的拦截器，执行
        if (index >= interceptors.size()) {
            throw new IllegalStateException("index->" + index + " is larger than interceptor size");
        }
        IRouteInterceptor interceptor = interceptors.get(index);
        interceptor.intercept(this,callback);
    }

    @Override
    public void interrupt(@NonNull Throwable exception) {

    }

    @Override
    public JPostcard navigate() {
        return this.postcard;
    }
}