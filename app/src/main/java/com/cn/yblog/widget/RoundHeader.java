package com.cn.yblog.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cn.yblog.R;
import com.cn.yblog.util.SizeUtil;

/**
 * description: 圆形头像
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class RoundHeader extends ConstraintLayout {
    private ConstraintLayout mRoot;
    private TextView mTvName;

    public RoundHeader(@NonNull Context context) {
        this(context, null);
    }

    public RoundHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RoundHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.widget_round_header, this, true);

        mRoot = findViewById(R.id.wid_round_headed_root);
        mTvName = findViewById(R.id.wid_round_header_tv_name);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.RoundHeader);
        String name = t.getString(R.styleable.RoundHeader_android_name);
        float size = t.getDimension(R.styleable.RoundHeader_name_size, 12);
        t.recycle();

        mTvName.setText(name);
        mTvName.setTextSize(SizeUtil.px2sp(size));
    }

    public void setName(String name) {
        mTvName.setText(name);
    }

    public void setNameSize(float size) {
        mTvName.setTextSize(SizeUtil.sp2px(size));
    }

    public void setBackgroundTint(int color) {
        Drawable background = mRoot.getBackground();
        background.setTint(color);
    }
}
