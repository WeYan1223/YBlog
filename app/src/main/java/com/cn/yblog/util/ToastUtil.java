package com.cn.yblog.util;

import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.ColorInt;

import com.cn.yblog.widget.ToastView;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class ToastUtil {
    @ColorInt
    private static final int ERROR_COLOR = Color.parseColor("#dc4437");//FD4C5B

    private static Toast sToast;
    private static Toast sErrorToast;

    private ToastUtil() {

    }

    /**
     * 弹出时长为{@link Toast#LENGTH_SHORT}的普通Toast
     *
     * @param msg 将要弹出的消息
     */
    public static void show(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出时长为{@link Toast#LENGTH_LONG}的普通Toast
     *
     * @param msg 将要弹出的消息
     */
    public static void showLong(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    /**
     * 弹出时长为{@link Toast#LENGTH_SHORT}的普通Toast
     *
     * @param msgResId 将要弹出的消息的资源id
     */
    public static void show(int msgResId) {
        showToast(msgResId, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出时长为{@link Toast#LENGTH_LONG}的普通Toast
     *
     * @param msgResId 将要弹出的消息的资源id
     */
    public static void showLong(int msgResId) {
        showToast(msgResId, Toast.LENGTH_LONG);
    }

    /**
     * 弹出时长为{@link Toast#LENGTH_SHORT}的错误Toast
     *
     * @param errorMsg 将要弹出的错误消息
     */
    public static void showError(String errorMsg) {
        showErrorToast(errorMsg, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出时长为{@link Toast#LENGTH_LONG}的错误Toast
     *
     * @param errorMsg 将要弹出的错误消息
     */
    public static void showErrorLong(String errorMsg) {
        showErrorToast(errorMsg, Toast.LENGTH_LONG);
    }

    /**
     * 弹出时长为{@link Toast#LENGTH_SHORT}的错误Toast
     *
     * @param errorMsgResId 将要弹出的错误消息的资源id
     */
    public static void showError(int errorMsgResId) {
        showErrorToast(errorMsgResId, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出时长为{@link Toast#LENGTH_LONG}的错误Toast
     *
     * @param errorMsgResId 将要弹出的错误消息的资源id
     */
    public static void showErrorLong(int errorMsgResId) {
        showErrorToast(errorMsgResId, Toast.LENGTH_LONG);
    }

    private static void showToast(String msg, int duration) {
        if (sToast == null) {
            sToast = new Toast(AppUtil.getAppContext());
            sToast.setView(new ToastView(AppUtil.getAppContext()));
        }
        ((ToastView) sToast.getView()).setText(msg, 0, 0);
        sToast.setDuration(duration);
        sToast.show();
//        Toast.makeText(AppUtil.getAppContext(), msg, duration).show();
    }

    private static void showToast(int msgResId, int duration) {
        if (sToast == null) {
            sToast = new Toast(AppUtil.getAppContext());
            sToast.setView(new ToastView(AppUtil.getAppContext()));
        }
        ((ToastView) sToast.getView()).setText(msgResId, 0, 0);
        sToast.setDuration(duration);
        sToast.show();
//        Toast.makeText(AppUtil.getAppContext(), msgResId, duration).show();
    }

    private static void showErrorToast(String errorMsg, int duration) {
        if (sErrorToast == null) {
            sErrorToast = new Toast(AppUtil.getAppContext());
            sErrorToast.setView(new ToastView(AppUtil.getAppContext()));
        }
        ((ToastView) sErrorToast.getView()).setText(errorMsg, ERROR_COLOR, 0);
        sErrorToast.setDuration(duration);
        sErrorToast.show();
//        Toast.makeText(AppUtil.getAppContext(), errorMsg, duration).show();
    }

    private static void showErrorToast(int errorMsgResId, int duration) {
        if (sErrorToast == null) {
            sErrorToast = new Toast(AppUtil.getAppContext());
            sErrorToast.setView(new ToastView(AppUtil.getAppContext()));
        }
        ((ToastView) sErrorToast.getView()).setText(errorMsgResId, ERROR_COLOR, 0);
        sErrorToast.setDuration(duration);
        sErrorToast.show();
//        Toast.makeText(AppUtil.getAppContext(), errorMsgResId, duration).show();
    }
}
