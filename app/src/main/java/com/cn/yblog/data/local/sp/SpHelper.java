package com.cn.yblog.data.local.sp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.StringDef;

import com.cn.yblog.util.AppUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class SpHelper {
    private static final String SP_NAME = "user_info";

    @StringDef({Key.KEY_USER_ID, Key.KEY_USERNAME, Key.KEY_PASSWORD, Key.KEY_LAST_LOGIN_TIME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Key {
        String KEY_USER_ID = "user_id";
        String KEY_USERNAME = "username";
        String KEY_PASSWORD = "password";
        String KEY_LAST_LOGIN_TIME = "last_login_time";
    }

    private SpHelper() {

    }

    /**
     * 获取
     *
     * @param key      键
     * @param defValue 默认值
     * @return {@code key}所对应的value，没有则返回默认值{@code defValue}
     */
    public static Object get(@Key String key, Object defValue) {
        SharedPreferences sp = AppUtil.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, String.valueOf(defValue));
        if (defValue instanceof String) {
            return value;
        } else if (defValue instanceof Integer) {
            return Integer.valueOf(value);
        } else if (defValue instanceof Boolean) {
            return Boolean.valueOf(value);
        } else if (defValue instanceof Float) {
            return Float.valueOf(value);
        } else if (defValue instanceof Long) {
            return Long.valueOf(value);
        } else if (defValue instanceof Double) {
            return Double.valueOf(value);
        }
        return defValue;
    }

    /**
     * 存储
     *
     * @param key   键
     * @param value 值
     */
    public static void put(@Key String key, Object value) {
        SharedPreferences sp = AppUtil.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }

    /**
     * 移除key所对应的值
     *
     * @param key 键
     */
    public static void remove(@Key String key) {
        SharedPreferences sp = AppUtil.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences sp = AppUtil.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
