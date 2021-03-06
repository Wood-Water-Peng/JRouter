package com.example.jrouterapi;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/18 10:22 AM
 * @Version 1.0
 * 存储路由信息的仓库
 */
public class JRouterWarehouse {
    //每一个module中的路由信息
    //home-->/home/homeActivity
    //       /home/home2Activity
    private static final Map<String, IRouteMap> routeModule = new HashMap<>();
    //每一个具体的路由信息
    ///home/homeActivity-> JPostcard存在放路由信息
    private static final Map<String, JPostcard> routeMap = new HashMap<>();

    public static void injectModule(IRouteModule module) {
        module.loadRouteModule(routeModule);
    }

    public static void injectRouteMap(String path, String moduleName) {
        if (routeMap.get(path) != null) {
            //已经注入
            return;
        }
        IRouteMap iRouteMap = routeModule.get(moduleName);
        if (iRouteMap == null) {
            throw new IllegalArgumentException("can not find moduleName->"+moduleName);
        }
        iRouteMap.loadInto(routeMap);
    }

    public static JPostcard getJPostCard(String path) {
        JPostcard postcard = routeMap.get(path);
        if (postcard == null) {
            throw new IllegalArgumentException("path->" + path + " is invalidate");
        }
        return postcard;
    }
}
