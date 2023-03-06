package com.cn.yblog.util;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/26
 * <p>version: 1.0
 * <p>update: none
 */
public class TextUtil {
    private TextUtil() {

    }

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        return s.trim().length() == 0;
    }
}
