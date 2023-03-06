package com.cn.yblog.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class TimeUtil {
    public static final int MILLIS_ONE_DAY = 24 * 60 * 60 * 1000;
    public static final int MILLIS_ONE_HOUR = 60 * 60 * 1000;

    private TimeUtil() {

    }

    public static long getCurMills() {
        return System.currentTimeMillis();
    }

    public static String getFormatTime(long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(date);
    }
}
