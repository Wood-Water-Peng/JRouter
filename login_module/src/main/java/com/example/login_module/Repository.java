package com.example.login_module;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.login_module_export.User;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 11:41 AM
 * @Version 1.0
 */
public class Repository {

    public static final String SP_KEY_UID = "sp_key_uid";
    public static final String SP_KEY_NAME = "sp_key_name";
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

    private String getUid() {
        return sharedPref.getString(SP_KEY_UID, "");
    }

    private String getToken() {
        return sharedPref.getString(SP_KEY_NAME, "");
    }

    private void putUid(String value) {
        sharedPref.edit().putString(SP_KEY_UID, value).apply();
    }

    private void putUserName(String userName) {
        sharedPref.edit().putString(SP_KEY_NAME, userName).apply();
    }

    MutableLiveData<User> mutableLiveData = new MutableLiveData<>();

    public void login(String name, String psw) {
        user = new User(name);
        mutableLiveData.postValue(new User(user.getName()));
        //保存在SP
        putUid(name);
    }

    public void logout() {
        user = null;
        mutableLiveData.postValue(null);
        //更新SP
        putUid("");
    }


    private String getUidFromSp() {
        return getUid();
    }

    public User getUser() {
        if (user == null) {
            String uid = getUidFromSp();
            if (!TextUtils.isEmpty(uid)) {
                user = new User(uid);
            }
        }
        //clone
        if (user != null) {
            return new User(user.getName());
        }
        return null;
    }

    public MutableLiveData<User> getMutableLiveData() {
        return mutableLiveData;
    }
}
