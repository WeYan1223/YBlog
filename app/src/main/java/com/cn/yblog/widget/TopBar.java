package com.cn.yblog.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cn.yblog.R;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/24
 * <p>version: 1.0
 * <p>update: none
 */
public class TopBar extends ConstraintLayout {
    private OnImbClickListener mOnImbClickListener;
    private TextView mTvTitle;

    public TopBar(@NonNull Context context) {
        this(context, null);
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.widget_top_bar, this, true);

        ImageButton imbLeft = findViewById(R.id.wid_top_bar_imb_left);
        ImageButton imbRight = findViewById(R.id.wid_top_bar_imb_right);
        mTvTitle = findViewById(R.id.wid_top_bar_tv_title);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        boolean showImbLeft = t.getBoolean(R.styleable.TopBar_show_left_imb, false);
        boolean showImbRight = t.getBoolean(R.styleable.TopBar_show_right_imb, false);
        int srcImbLeft = t.getResourceId(R.styleable.TopBar_left_imb_src, -1);
        int srcImbRight = t.getResourceId(R.styleable.TopBar_right_imb_src, -1);
        String title = t.getString(R.styleable.TopBar_android_title);
        t.recycle();

        if (showImbLeft) {
            imbLeft.setVisibility(VISIBLE);
            imbLeft.setImageResource(srcImbLeft);
        }
        if (showImbRight) {
            imbRight.setVisibility(VISIBLE);
            imbRight.setImageResource(srcImbRight);
        }
        mTvTitle.setText(title);

        imbLeft.setOnClickListener(v -> {
            if (mOnImbClickListener != null) {
                mOnImbClickListener.onLeftImbClick();
            }
        });
        imbRight.setOnClickListener(v -> {
            if (mOnImbClickListener != null) {
                mOnImbClickListener.onRightImbClick();
            }
        });
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    /**
     * 设置左右侧按钮点击事件
     *
     * @param onImbClickListener OnImbClickListener
     */
    public void setOnImbClickListener(OnImbClickListener onImbClickListener) {
        mOnImbClickListener = onImbClickListener;
    }

    public interface OnImbClickListener {
        /**
         * 点击左侧按钮
         */
        void onLeftImbClick();

        /**
         * 点击右侧按钮
         */
        void onRightImbClick();
    }
}
