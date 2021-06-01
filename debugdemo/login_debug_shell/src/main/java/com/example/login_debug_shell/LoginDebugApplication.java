package com.example.login_debug_shell;

import android.app.Application;

import com.example.jrouterapi.core.JRouter;
import com.example.jrouterapi.module.ModuleHelper;

public class LoginDebugApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JRouter.init(this);
        ModuleHelper.registerModule("login_module");
    }
}
