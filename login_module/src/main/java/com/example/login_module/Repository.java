package com.example.login_module;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 11:41 AM
 * @Version 1.0
 */
public class Repository {

    public static final String SP_KEY_UID="sp_key_uid";
    public static final String SP_KEY_TOKEN="sp_key_token";
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
        sharedPref.edit().putString(key, value).apply();
    }

    public String getUid() {
        return sharedPref.getString(SP_KEY_UID, "");
    }

    public String getToken() {
        return sharedPref.getString(SP_KEY_TOKEN, "");
    }

    public void putUid(String value) {
         sharedPref.edit().putString(SP_KEY_UID, value).apply();
    }
    public void putToken(String token) {
        sharedPref.edit().putString(SP_KEY_TOKEN, token).apply();
    }
}
