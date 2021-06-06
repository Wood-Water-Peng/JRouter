package com.example.home_module;

import android.content.Context;

import com.example.annotation.JModuleAnno;
import com.example.base_lib.JLogUtil;
import com.example.jrouterapi.module.IModuleInterface;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 7:51 PM
 * @Version 1.0
 */
@JModuleAnno
public class HomeModule implements IModuleInterface {
    public static Context sContext;

    @Override
    public void onCreated(Context context) {
        JLogUtil.log(JLogUtil.MODULE_TAG, "HomeModule onCreated");
        sContext = context;
    }

    @Override
    public void onDestroy() {
        JLogUtil.log(JLogUtil.MODULE_TAG, "HomeModule onDestroy");
    }
}
