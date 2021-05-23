package com.example.jrouterapi.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.jrouterapi.IRouteModule;
import com.example.jrouterapi.JRouterWarehouse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * @Author jacky.peng
 * @Date 2021/5/22 6:15 PM
 * @Version 1.0
 */
public class JRouteHelper {
    public static final String TAG = "JRouteHelper";

    //找到apt生成的所有模块辅助类，理论上每一个模块一个辅助类
    public static void loadRoute(Context context) {
        try {
            List<String> className = getClassName(context, "com.example.jrouter");
            for (String name :
                    className) {
                if (name.startsWith("com.example.jrouter.route_modules.JRouter$$RouterModule")) {
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

    static List<String> getClassName(Context context, String packageName) throws PackageManager.NameNotFoundException {

        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> classNameList = new ArrayList<>();
        try {
            DexFile df = new DexFile(applicationInfo.sourceDir);//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {
                String className = enumeration.nextElement();
                if (className.contains(packageName)) {
                    classNameList.add(className);
                    Log.d(TAG, String.format("加载{%s}", className));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNameList;
    }

}
