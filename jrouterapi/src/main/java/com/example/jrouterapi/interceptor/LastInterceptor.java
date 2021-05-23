package com.example.jrouterapi.interceptor;

import androidx.annotation.NonNull;

/**
 * @Author jacky.peng
 * @Date 2021/5/23 5:15 PM
 * @Version 1.0
 */
public class LastInterceptor implements IRouteInterceptor {

    @Override
    public void intercept(@NonNull Chain chain, @NonNull Callback callback) {

        callback.onSuccess(chain.navigate());
    }
}
