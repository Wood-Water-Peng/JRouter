package com.example.login_module;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 11:41 AM
 * @Version 1.0
 */
public class Repository {

    Context context;
    SharedPreferences sharedPref;

    private static Repository sRepository;

    public static Repository getInstance() {
        if (sRepository == null) {
            sRepository = new Repository(UserModule.sContext);
        }
        return sRepository;
    }

    private Repository(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("jrouter_sp", Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        sharedPref.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return sharedPref.getString(key, "");
    }
}
