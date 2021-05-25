package com.example.jrouter;

import android.app.Application;

import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.module.ModuleHelper;

/**
 * @Author jacky.peng
 * @Date 2021/5/22 6:31 PM
 * @Version 1.0
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JRouter.init(this);
//        ModuleHelper.registerModule("home_module");
        ModuleHelper.registerModule("login_module");
    }
}
