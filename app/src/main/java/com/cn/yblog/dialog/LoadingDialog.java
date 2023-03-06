package com.cn.yblog.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.cn.yblog.R;
import com.cn.yblog.util.AppUtil;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/23
 * <p>version: 1.0
 * <p>update: none
 */
public class LoadingDialog extends DialogFragment {
    /**
     * 默认点击外面无效
     */
    private boolean onTouchOutside = false;

    /**
     * 默认加载框提示信息
     */
    private String mHint = AppUtil.getString(R.string.word_loading);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(onTouchOutside);
        View loadingView = inflater.inflate(R.layout.dialog_loading, container);
        TextView hintTextView = loadingView.findViewById(R.id.dialog_loading_tv_hint);
        hintTextView.setText(mHint);
        return loadingView;
    }

    /**
     * 设置是否允许点击外面取消
     */
    public LoadingDialog setOnTouchOutside(boolean onTouchOutside) {
        this.onTouchOutside = onTouchOutside;
        return this;
    }

    /**
     * 设置加载框提示信息
     */
    public LoadingDialog setHint(String hint) {
        if (!TextUtils.isEmpty(hint)) {
            this.mHint = hint;
        }
        return this;
    }
}