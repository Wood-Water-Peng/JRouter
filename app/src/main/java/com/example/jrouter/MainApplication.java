package com.example.jrouter;

import android.app.Application;

import com.example.base_lib.JLogUtil;
import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.module.ModuleHelper;
import com.example.perttask.PertGraph;
import com.example.perttask.PertGraphManager;
import com.example.perttask.Task;

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
        ModuleHelper.registerModule("home_module");

        PertGraph mainApplication_graph = new PertGraph("MainApplication graph");
        mainApplication_graph.addTask(new Task("MainApplicationTask") {
            @Override
            public void call() {
                JLogUtil.log(getName() + " is running...on thread ->"+Thread.currentThread().getName());
            }
        });
//        PertGraphManager.getInstance().addGraphTask(mainApplication_graph).start();
    }
}
