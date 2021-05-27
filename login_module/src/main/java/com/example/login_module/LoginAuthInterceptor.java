package com.example.login_module;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.annotation.InterceptorAnno;
import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.interceptor.IRouteInterceptor;

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
        if(checkToken(Repository.getInstance().getUid(),Repository.getInstance().getToken())){
            chain.proceed(jPostcard);
        }else {
            Exception exception = new Exception("invalidate uid");
            chain.interrupt(exception);
        }
    }

    private boolean checkToken(String uid, String token) {
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(token)) return false;
        if (uid.equals("admin") && token.equals("admin")) {
            return true;
        }
        return false;
    }
}
