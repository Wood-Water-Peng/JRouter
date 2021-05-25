package com.example.home_module;

import androidx.annotation.NonNull;

import com.example.annotation.InterceptorAnno;
import com.example.jrouterapi.interceptor.IRouteInterceptor;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 9:02 AM
 * @Version 1.0
 */
@InterceptorAnno(path = "")
public class HomeGlobalInterceptor implements IRouteInterceptor {
    @Override
    public void intercept(@NonNull Chain chain, @NonNull Callback callback) {

    }
}
