package com.example.login_module;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.login_module_export.User;
import com.google.gson.Gson;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 11:41 AM
 * @Version 1.0
 */
public class Repository {

    public static final String SP_KEY_UID = "sp_key_uid";
    Context context;
    SharedPreferences sharedPref;
    User user;
    private static Repository sRepository;

    public static Repository getInstance() {
        if (sRepository == null) {
            if (UserModule.sContext == null) {
                throw new IllegalStateException("you should init corresponding module first");
            }
            sRepository = new Repository(UserModule.sContext);
        }
        return sRepository;
    }

    private Repository(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        sharedPref.edit().putString(key, value).apply();
    }

    private String getUserFromSp() {
        return sharedPref.getString(SP_KEY_UID, "");
    }


    private void putUerInfo(String value) {
        sharedPref.edit().putString(SP_KEY_UID, value).apply();
    }

    MutableLiveData<User> mutableLiveData = new MutableLiveData<>();

    public void login(String name) {
        user = new User(name);
        if (name.equals("admin")) {
            user.setLevel(2);
        } else if (name.equals("jacky")) {
            user.setLevel(1);
        } else {
            user.setLevel(0);
        }
        user.setLogin(true);
        Gson gson = new Gson();
        mutableLiveData.postValue(new User(user.getName(),user.getLevel()));
        //保存在SP
        putUerInfo(gson.toJson(user));
    }

    public void logout() {
        user = null;
        mutableLiveData.postValue(null);
        //更新SP
        putUerInfo("");
    }


    public User getUser() {
        if (user == null) {
            String userJson = getUserFromSp();
            if (!TextUtils.isEmpty(userJson)) {
                Gson gson = new Gson();
                user = gson.fromJson(userJson, User.class);
            }
        }
        //clone
        if (user != null) {
            return new User(user.getName(),user.getLevel());
        }
        return null;
    }

    public MutableLiveData<User> getMutableLiveData() {
        return mutableLiveData;
    }
}
