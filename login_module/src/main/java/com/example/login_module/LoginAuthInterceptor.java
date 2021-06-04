package com.example.login_module;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.annotation.InterceptorAnno;
import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.interceptor.IRouteInterceptor;
import com.example.jrouterapi.service.ServiceCenter;
import com.example.login_module_export.IUserService;

/**
 * @Author jacky.peng
 * @Date 2021/5/25 11:24 AM
 * @Version 1.0
 */
@InterceptorAnno(path = "/login_module/UserInfoActivity")
public class LoginAuthInterceptor implements IRouteInterceptor {
    @Override
    public void intercept(@NonNull Chain chain, @NonNull Callback callback) {
        JPostcard jPostcard = chain.navigate();
        IUserService userService = (IUserService) ServiceCenter.getService(IUserService.name);
        if (userService != null && userService.getUser() != null) {
            chain.proceed(jPostcard);
        } else {
            Exception exception = new Exception("login first");
            chain.interrupt(exception);
        }
    }
}
