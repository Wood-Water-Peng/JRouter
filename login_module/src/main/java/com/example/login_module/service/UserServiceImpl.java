package com.example.login_module.service;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.annotation.JServiceAnno;
import com.example.login_module.Repository;
import com.example.login_module_export.IUserService;
import com.example.login_module_export.User;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 3:42 PM
 * @Version 1.0
 * <p>
 * 模块对外提供的服务，在模块被加载时，注册到服务中心
 */
@JServiceAnno(name = IUserService.name)
public class UserServiceImpl implements IUserService {


    @Override
    public User getUser() {
        return Repository.getInstance().getUser();
    }

    @Override
    public MutableLiveData<User> getUserLiveData() {
        return Repository.getInstance().getMutableLiveData();
    }

    @Override
    public void logout() {
        Repository.getInstance().logout();
    }
}
