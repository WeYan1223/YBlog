package com.cn.yblog.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;

import com.cn.yblog.util.SizeUtil;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/27
 * <p>version: 1.0
 * <p>update: none
 */
public final class ToastView extends LinearLayout {
    @ColorInt
    private static final int DEFAULT_BG_COLOR = Color.parseColor("#E6555555");
    private static final String TOAST_TYPEFACE = "sans-serif";
    private AppCompatImageView iconView;
    private TextView textView;
    private GradientDrawable backgroundDrawable;

    public ToastView(Context context) {
        super(context);
        //布局
        int paddingH = SizeUtil.dp2px(23);
        int paddingV = SizeUtil.dp2px(15);
        setPadding(paddingH, paddingV, paddingH, paddingV);
        iconView = new AppCompatImageView(context);
        textView = new TextView(context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        int iconSize = SizeUtil.dp2px(24);
        LayoutParams iconParams = new LayoutParams(iconSize, iconSize);
        iconParams.setMarginEnd(SizeUtil.dp2px(8));
        addView(iconView, iconParams);
        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setGravity(Gravity.CENTER);
        addView(textView, textParams);

        //设置背景，设置背景tint
        backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setCornerRadius(SizeUtil.dp2px(40));
        backgroundDrawable.setColor(DEFAULT_BG_COLOR);
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        backgroundDrawable.setAlpha(230);
        setBackground(backgroundDrawable);

        //设置icon
        iconView.setVisibility(View.GONE);

        //设置文字
        //sans-serif-condensed是系统默认
        textView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
    }

    public void setText(@StringRes int msg, @ColorInt int bgTint, @DrawableRes int iconRes) {
        setText(getContext().getString(msg), bgTint, iconRes);
    }

    public void setText(@NonNull CharSequence msg, @ColorInt int bgTint, @DrawableRes int iconRes) {
        if (bgTint == 0) {
            backgroundDrawable.setColor(DEFAULT_BG_COLOR);
        } else {
            backgroundDrawable.setColor(bgTint);
        }
        if (iconRes == 0) {
            iconView.setVisibility(GONE);
        } else {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageResource(iconRes);
        }
        textView.setText(msg);
    }
}
