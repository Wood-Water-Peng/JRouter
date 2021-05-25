package com.example.jrouterapi.interceptor;

import android.provider.SyncStateContract;

import com.example.jrouterapi.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 2:01 PM
 * @Version 1.0
 */
public class JInterceptorHelper {
    //加载这个模块中的拦截器
    public static void addModuleInterceptor(String moduleName) {
        try {
            Class<?> aClass = Class.forName("com.example.jrouter.interceptor_modules.JRouter$$Interceptor$$" + moduleName);
            IModuleInterceptor moduleInterceptor = (IModuleInterceptor) aClass.getConstructor().newInstance();
            Map<String, IRouteInterceptor> interceptors = moduleInterceptor.interceptors();
            for (Map.Entry<String, IRouteInterceptor> entry : interceptors.entrySet()
            ) {
                JInterceptorStore.putInterceptor(entry.getKey(), entry.getValue());
            }
            //全局拦截器   暂时不添加模块中定义的全局拦截器
//            JInterceptorStore.putGlobalInterceptors(moduleInterceptor.moduleGlobalInterceptors());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //卸载模块中的拦截器  暂时无法实现
    public static void removeModuleInterceptor(String moduleName) {

    }
}
