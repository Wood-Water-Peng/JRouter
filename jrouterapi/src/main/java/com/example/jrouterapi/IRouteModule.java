package com.example.jrouterapi;

import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/18 10:46 AM
 * @Version 1.0
 * <p>
 * home--->homeRouteModule      home模块的路由信息
 */
public interface IRouteModule {
    //加载某一个模块的路由信息，在编译期由JRouterWarehouse去加载每一个模块的路由模块，但是模块中具体的新药要等到模块中路由被使用时才加载
    void loadRouteModule(Map<String, IRouteMap> routeModule);
}
