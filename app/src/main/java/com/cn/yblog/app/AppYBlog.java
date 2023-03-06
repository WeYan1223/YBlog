package com.cn.yblog.app;

import android.app.Application;
import android.webkit.WebView;

import com.cn.yblog.util.AppUtil;
import com.squareup.leakcanary.LeakCanary;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class AppYBlog extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.init(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        WebView
        LeakCanary.install(this);
    }
}
