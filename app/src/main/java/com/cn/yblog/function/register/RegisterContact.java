package com.cn.yblog.function.register;

import com.cn.yblog.base.IBasePresenter;
import com.cn.yblog.base.IBaseView;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/24
 * <p>version: 1.0
 * <p>update: none
 */
public interface RegisterContact {
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
         * 显示注册加载框
         */
        void showLogonDialog();

        /**
         * 隐藏注册加载框
         */
        void dismissLogonDialog();

        /**
         * 跳转回登录界面
         * <p>只有注册成功才通过该方法返回登陆界面
         *
         * @param username 用户名 用于填充登录界面
         * @param password 密码 用于填充登录界面
         */
        void backToLogin(String username, String password);
    }

    interface Presenter extends IBasePresenter<View> {
        /**
         * 注册
         *
         * @param username   用户名
         * @param password   密码
         * @param rePassword 确认密码
         */
        void register(String username, String password, String rePassword);
    }
}
