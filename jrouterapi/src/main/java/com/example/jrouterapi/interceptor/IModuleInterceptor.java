package com.example.jrouterapi.interceptor;

import java.util.List;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 8:55 AM
 * @Version 1.0
 * 模块中的拦截器，注解处理器使用
 */
public interface IModuleInterceptor {
    //模块中每一个路由对应的特有拦截器
    Map<String, IRouteInterceptor> interceptors();

    //模块中的全局拦截器
    List<IRouteInterceptor> moduleGlobalInterceptors();
}
