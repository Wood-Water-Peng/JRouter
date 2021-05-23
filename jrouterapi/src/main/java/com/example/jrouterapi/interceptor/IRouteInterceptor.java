package com.example.jrouterapi.interceptor;

import androidx.annotation.NonNull;

import com.example.jrouterapi.JPostcard;
import com.example.jrouterapi.core.JRouter;

/**
 * @Author jacky.peng
 * @Date 2021/5/23 5:11 PM
 * @Version 1.0
 * 拦截器接口
 */
public interface IRouteInterceptor {

    void intercept(@NonNull Chain chain,@NonNull Callback callback);

    interface Chain {
        /**
         * 如果拦截器决定不拦截，调用这个方法
         */
        void proceed(@NonNull JPostcard jPostcard);

        /**
         * 如果拦截器决定拦截，调用这个方法
         */
        void interrupt(@NonNull Throwable exception);

        /**
         * 返回路由对象
         */
        JPostcard navigate();

    }

    interface Callback {
        /**
         * 跳转成功
         */
        void onSuccess(@NonNull JPostcard jPostcard);

        /**
         * 跳转失败，被拦截器拦下了
         */
        void onFail(@NonNull Throwable exception);
    }

}
