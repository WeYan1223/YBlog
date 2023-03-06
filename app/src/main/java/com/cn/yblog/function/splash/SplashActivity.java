package com.cn.yblog.function.splash;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cn.yblog.R;
import com.cn.yblog.data.local.sp.SpHelper;
import com.cn.yblog.data.remote.db.GaussHelper;
import com.cn.yblog.function.login.LoginActivity;
import com.cn.yblog.function.main.MainActivity;
import com.cn.yblog.service.RemoteService;
import com.cn.yblog.util.LogUtil;
import com.cn.yblog.util.TimeUtil;
import com.cn.yblog.util.ToastUtil;

import java.sql.SQLException;

/**
 * description: 启动页跳转规则：
 * <p>本地没有缓存用户数据->LoginActivity
 * <p>本地有缓存用户数据->发送登录请求
 * <p>  请求成功->MainActivity
 * <p>  请求失败->LoginActivity
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler(Looper.getMainLooper());
        //连接数据库
        RemoteService.getInstance().execute(() -> {
            try {
                GaussHelper.getInstance().connect();
            } catch (ClassNotFoundException e) {
                LogUtil.e("连接数据库失败：找不到驱动类com.huawei.opengauss.jdbc.Driver");
                handler.post(() -> ToastUtil.show(R.string.splash_error_db_connect_failure));
                e.printStackTrace();
                return;
            } catch (SQLException e) {
                LogUtil.e("连接数据库失败：服务器方面出现问题");
                handler.post(() -> ToastUtil.show(R.string.splash_error_db_connect_failure));
                e.printStackTrace();
                return;
            }
            GaussHelper.getInstance().startDaemon();
            handler.post(() -> ToastUtil.show(R.string.splash_normal_db_connect_success));
            int userId = (int) SpHelper.get(SpHelper.Key.KEY_USER_ID, -1);
            if (userId == -1 || TimeUtil.getCurMills() - (long) (SpHelper.get(SpHelper.Key.KEY_LAST_LOGIN_TIME, Long.MIN_VALUE)) > TimeUtil.MILLIS_ONE_DAY) {
                handler.post(() -> LoginActivity.startAction(this));
                return;
            }
            long lastLoginTime = (long) SpHelper.get(SpHelper.Key.KEY_LAST_LOGIN_TIME, Long.MIN_VALUE);
            if (TimeUtil.getCurMills() - lastLoginTime > TimeUtil.MILLIS_ONE_DAY) {
                handler.post(() -> LoginActivity.startAction(this));
                return;
            }
            if (GaussHelper.getInstance().getDaoUser().get(userId) == null) {
                handler.post(() -> LoginActivity.startAction(this));
            } else {
                SpHelper.put(SpHelper.Key.KEY_LAST_LOGIN_TIME, TimeUtil.getCurMills());
                handler.post(() -> MainActivity.startAction(this));
            }
        });
    }
}
