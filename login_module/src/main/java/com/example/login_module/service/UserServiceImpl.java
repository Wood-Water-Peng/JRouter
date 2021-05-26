package com.example.login_module.service;

import android.text.TextUtils;

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
public class UserServiceImpl implements IUserService {
    @Override
    public User getUser() {
        String uid = Repository.getInstance().getUid();
        String token = Repository.getInstance().getToken();
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(token)) {
            return new User(uid, token);
        }
        return null;
    }
}
