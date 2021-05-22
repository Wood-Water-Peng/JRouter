package com.example.jrouterapi.core;

import android.app.Application;
import android.content.Context;

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
}
