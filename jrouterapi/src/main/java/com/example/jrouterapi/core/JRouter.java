package com.example.jrouterapi.core;

import android.app.Application;
import android.content.Context;

import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.JRouterWarehouse;

/**
 * @Author jacky.peng
 * @Date 2021/5/22 6:11 PM
 * @Version 1.0
 */
public class JRouter {

    private static Context sContext;

    private static final class Holder {
        private static final JRouter instance = new JRouter();
    }

    public static JRouter getInstance() {
        return Holder.instance;
    }

    public static void init(Application context) {
        sContext = context;
        JRouteHelper.loadRoute(sContext);
    }

    /**
     * @param path /home/homeActivity
     */
    public static JPostcard path(String path) {
        String moduleName = parseGroup(path);
        //此时module中的路由信息可能还没有被注入
        JRouterWarehouse.injectRouteMap(path, moduleName);
        //生成路由信息
        JPostcard jPostCard = JRouterWarehouse.getJPostCard(path);
        return jPostCard;
    }

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
