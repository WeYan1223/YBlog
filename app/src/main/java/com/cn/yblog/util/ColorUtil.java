package com.cn.yblog.util;

import com.cn.yblog.R;

/**
 * description: 颜色工具类
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class ColorUtil {
    private static final int[] sColors = new int[]{
            AppUtil.getAppContext().getResources().getColor(R.color.red_200),
            AppUtil.getAppContext().getResources().getColor(R.color.pink_200),
            AppUtil.getAppContext().getResources().getColor(R.color.purple_200),
            AppUtil.getAppContext().getResources().getColor(R.color.blue_400),
            AppUtil.getAppContext().getResources().getColor(R.color.cyan_200),
            AppUtil.getAppContext().getResources().getColor(R.color.green_200),
            AppUtil.getAppContext().getResources().getColor(R.color.yellow_200),
            AppUtil.getAppContext().getResources().getColor(R.color.amber_200),
            AppUtil.getAppContext().getResources().getColor(R.color.grange_200),
    };

    private ColorUtil() {

    }

    /**
     * 随机获取{@link ColorUtil#sColors}中的一种颜色
     *
     * @return 颜色
     */
    public static int getColor(int position) {
        return sColors[position % sColors.length];
    }
}
