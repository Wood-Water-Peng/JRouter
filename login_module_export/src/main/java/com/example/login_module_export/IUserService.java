package com.example.login_module_export;

import androidx.lifecycle.MutableLiveData;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 3:44 PM
 * @Version 1.0
 */
public interface IUserService {
    String name = "login_module_user_service";

    User getUser();

    MutableLiveData<User> getUserLiveData();

    public void logout();
}
