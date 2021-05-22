package com.example.jrouter;

import android.app.Application;

import com.example.jrouterapi.core.JRouter;

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
    }
}
