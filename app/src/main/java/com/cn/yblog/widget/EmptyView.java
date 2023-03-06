package com.cn.yblog.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
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
public class EmptyView extends ConstraintLayout {
    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.widget_empty_view, this, true);

        ImageView imvIcon = findViewById(R.id.wid_empty_view_imv_icon);
        TextView tvSummary = findViewById(R.id.wid_empty_view_tv_summary);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);
        int src = t.getResourceId(R.styleable.EmptyView_src_icon, -1);
        String summary = t.getString(R.styleable.EmptyView_android_summary);
        t.recycle();

        if (src != -1) {
            imvIcon.setImageResource(src);
        }
        if (summary != null) {
            tvSummary.setText(summary);
        }
    }
}
