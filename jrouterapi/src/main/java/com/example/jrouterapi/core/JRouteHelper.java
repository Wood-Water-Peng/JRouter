package com.example.jrouterapi.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.jrouterapi.IRouteModule;
import com.example.jrouterapi.JRouterWarehouse;
import com.example.jrouterapi.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/5/22 6:15 PM
 * @Version 1.0
 */
public class JRouteHelper {
    public static final String TAG = "JRouteHelper";

    //找到apt生成的所有模块辅助类，理论上每一个模块一个辅助类
    public static void loadRoute(Context context) {
        injectRouteModuleByPlugin();

    }

    private void injectRouteModule(Context context) {
        try {
            List<String> className = Utils.getClassName(context, "com.example.jrouter");
            for (String name :
                    className) {
                if (name.startsWith("com.example.jrouter.JRouter$$RouterModule")) {
                    IRouteModule routeModule = (IRouteModule) Class.forName(name).getConstructor().newInstance();
                    JRouterWarehouse.injectModule(routeModule);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用插桩在JRouteHelper的字节码中加入加载路由模块的代码
     */
    public static void injectRouteModuleByPlugin() {
//       register(new Test());
//        register(new JRouter$$RouterModule$$home_module());
    }

    //for asm
    private static void register(IRouteModule module) {
        Log.i(TAG,"register->"+module.getClass().getCanonicalName());
        JRouterWarehouse.injectModule(module);
    }
}
