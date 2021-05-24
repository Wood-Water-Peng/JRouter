package com.example.jrouterapi.module;

import android.app.Application;
import android.content.Context;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 7:50 PM
 * @Version 1.0
 */
public interface IModuleInterface {
    void onCreated(Application context);

    void onDestroy();
}
