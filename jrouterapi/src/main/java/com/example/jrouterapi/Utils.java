package com.example.jrouterapi;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * @Author jacky.peng
 * @Date 2021/5/24 5:45 PM
 * @Version 1.0
 */
public class Utils {
    public static List<String> getClassName(Context context, String packageName) throws PackageManager.NameNotFoundException {

        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> classNameList = new ArrayList<>();
        try {
            DexFile df = new DexFile(applicationInfo.sourceDir);//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {
                String className = enumeration.nextElement();
                if (className.contains(packageName)) {
                    classNameList.add(className);
                    Log.d("getClassName", String.format("加载{%s}", className));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNameList;
    }

}
