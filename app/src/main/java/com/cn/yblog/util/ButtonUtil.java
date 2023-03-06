package com.cn.yblog.util;

import com.cn.yblog.R;

/**
 * description: 按钮工具类
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/26
 * <p>version: 1.0
 * <p>update: none
 */
public class ButtonUtil {
    /**
     * 两次按钮点击的最大时间差1000ms
     */
    private static final long TIME_INTERVAL = 500;

    /**
     * 上一次点击时间
     */
    private static long lastClickTime = 0;

    /**
     * 上一次点击的按钮ID
     */
    private static int lastClickButtonId = -1;

    private ButtonUtil() {

    }

    /**
     * 判断是否快速点击按钮
     *
     * @param buttonId 按钮ID
     * @return true表示快速点击
     */
    public static boolean isFastClick(int buttonId) {
        boolean result = false;
        long cur = TimeUtil.getCurMills();
        long interval = cur - lastClickTime;
        if (lastClickButtonId != buttonId && interval < TIME_INTERVAL) {
            result = true;
        }
        lastClickTime = cur;
        lastClickButtonId = buttonId;
        if (result) {
            ToastUtil.showError(R.string.error_fast_click);
        }
        return result;
    }
}
