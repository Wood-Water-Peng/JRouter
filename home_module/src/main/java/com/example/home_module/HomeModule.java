package com.example.home_module;

import android.app.Application;

import com.example.annotation.JModule;
import com.example.jrouterapi.module.IModuleInterface;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 7:51 PM
 * @Version 1.0
 */
@JModule
public class HomeModule implements IModuleInterface {
    @Override
    public void onCreated(Application context) {

    }

    @Override
    public void onDestroy() {

    }
}
