package com.cn.yblog.util;

import android.app.Application;
import android.content.Context;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class AppUtil {
    private static Context sApplicationContext;

    private AppUtil() {

    }

    /**
     * 初始化AppUtil，必须在{@link Application#onCreate()}时调用
     *
     * @param context Context
     */
    public static void init(Context context) {
        sApplicationContext = context.getApplicationContext();
    }

    /**
     * 获取Application类型的Context
     *
     * @return Context
     */
    public static Context getAppContext() {
        if (sApplicationContext == null) {
            throw new NullPointerException("AppUtil没有被正确初始化");
        } else {
            return sApplicationContext;
        }
    }

    public static String getString(int stringId) {
        return sApplicationContext.getResources().getString(stringId);
    }
}
