package com.example.jrouterapi.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 1:55 PM
 * @Version 1.0
 * <p>
 * 拦截器容器，用于存放所有模块中的拦截器
 */
public class JInterceptorStore {
    //每个路径特有的拦截器
    static Map<String, IRouteInterceptor> pathInterceptor = new HashMap<>();

    //全局拦截器
    static List<IRouteInterceptor> globalInterceptors = new ArrayList<>();

    public static void putInterceptor(String path, IRouteInterceptor interceptor) {
        pathInterceptor.put(path, interceptor);
    }

    public static void putGlobalInterceptors(List<IRouteInterceptor> interceptors) {
        globalInterceptors.addAll(interceptors);
    }

    public static Map<String, IRouteInterceptor> getPathInterceptor() {
        return pathInterceptor;
    }

    public static List<IRouteInterceptor> getGlobalInterceptors() {
        return globalInterceptors;
    }
}
