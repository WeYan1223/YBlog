package com.cn.yblog.function.login;

import com.cn.yblog.base.IBasePresenter;
import com.cn.yblog.base.IBaseView;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public interface LoginContact {
    interface View extends IBaseView {
        /**
         * 弹出普通信息
         *
         * @param normalMsg 普通信息
         */
        void showNormal(String normalMsg);

        /**
         * 弹出错误信息
         *
         * @param errorMsg 错误信息
         */
        void showError(String errorMsg);

        /**
         * 显示登录加载框
         */
        void showLoginDialog();

        /**
         * 隐藏登录加载框
         */
        void dismissLoginDialog();

        /**
         * 跳转到注册界面
         */
        void toRegister();

        /**
         * 跳转到主界面
         */
        void toMain();
    }

    interface Presenter extends IBasePresenter<View> {
        /**
         * 登录
         *
         * @param username 用户名
         * @param password 密码
         */
        void login(String username, String password);
    }
}