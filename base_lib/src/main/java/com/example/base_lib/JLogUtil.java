package com.example.base_lib;

import android.util.Log;

/**
 * @Author jacky.peng
 * @Date 2021/5/25 9:34 AM
 * @Version 1.0
 */
public class JLogUtil {
    public static final String MODULE_TAG = "MODULE_TAG";

    public static void log() {

    }

    public static void log(String tag, String msg) {
        Log.d(tag, msg);
    }
}
