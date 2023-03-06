package com.cn.yblog.util;

import android.util.Log;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class LogUtil {
    /**
     * 是否能输出日志/Todo(取决于当前是Debug还是Release版本)
     */
    private static boolean sLogEnable = true;

    private LogUtil() {

    }

    public static void setLogEnable(boolean enable) {
        sLogEnable = enable;
    }

    public static void e(String tag, String message) {
        if (sLogEnable) {
            Log.e(tag, message);
        }
    }

    public static void e(String message) {
        e("Error", message);
    }

    public static void w(String tag, String message) {
        if (sLogEnable) {
            Log.w(tag, message);
        }
    }

    public static void w(String message) {
        w("Warn", message);
    }

    public static void i(String tag, String message) {
        if (sLogEnable) {
            Log.i(tag, message);
        }
    }

    public static void i(String message) {
        i("Info", message);
    }

    public static void d(String tag, String message) {
        if (sLogEnable) {
            Log.d(tag, message);
        }
    }

    public static void d(String message) {
        d("Debug", message);
    }

    public static void v(String tag, String message) {
        if (sLogEnable) {
            Log.v(tag, message);
        }
    }

    public static void v(String message) {
        v("Verbose", message);
    }
}
