package com.example.login_module;

import android.content.Context;

import com.example.annotation.JModuleAnno;
import com.example.jrouterapi.module.IModuleInterface;

/**
 * @Author jacky.peng
 * @Date 2021/5/25 11:17 AM
 * @Version 1.0
 */
@JModuleAnno
public class UserModule implements IModuleInterface {
    @Override
    public void onCreated(Context context) {

    }

    @Override
    public void onDestroy() {

    }
}
