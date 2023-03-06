package com.cn.yblog.function.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.yblog.R;
import com.cn.yblog.base.BaseActivity;
import com.cn.yblog.dialog.LoadingDialog;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.widget.TopBar;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * description: 博客编辑界面
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class EditActivity extends BaseActivity<EditContact.Presenter> implements EditContact.View {
    @IntDef({Mode.PUBLISH, Mode.MODIFY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        int PUBLISH = 0x0001;
        int MODIFY = 0x0002;
    }

    private int mMode;
    private int mBlogId;

    private TopBar mTopBar;
    private EditText mEdtTitle;
    private EditText mEdtContent;
    private Button mBtnPublish;
    private LoadingDialog mLoadingDialog;

    /**
     * 启动编辑界面的唯一入口
     *
     * @param context 上下文
     * @param mode    发布或修改模式
     * @param blogId  修改模式下该值不能为空
     */
    public static void startAction(Context context, @Mode int mode, int blogId) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("mode", mode);
        if (mode == Mode.MODIFY) {
            intent.putExtra("blog_id", blogId);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initMode();
        initView();
        initListener();
    }

    private void initMode() {
        Intent intent = getIntent();
        mMode = intent.getIntExtra("mode", Mode.PUBLISH);
        if (mMode == Mode.MODIFY) {
            mBlogId = intent.getIntExtra("blog_id", -1);
        }
    }

    private void initView() {
        mTopBar = findViewById(R.id.edit_top_bar);
        mEdtTitle = ((TextInputLayout) findViewById(R.id.edit_til_title)).getEditText();
        mEdtContent = ((TextInputLayout) findViewById(R.id.edit_til_content)).getEditText();
        mBtnPublish = findViewById(R.id.edit_btn_publish);
        if (mMode == Mode.MODIFY) {
            mBtnPublish.setText(R.string.word_modify);
            getPresenter().loadBlog(mBlogId);
        }
    }

    private void initListener() {
        mTopBar.setOnImbClickListener(new TopBar.OnImbClickListener() {
            @Override
            public void onLeftImbClick() {
                back();
            }

            @Override
            public void onRightImbClick() {

            }
        });

        mBtnPublish.setOnClickListener(v -> {
            String title = mEdtTitle.getText().toString();
            String content = mEdtContent.getText().toString();
            if (mMode == Mode.PUBLISH) {
                getPresenter().publish(title, content);
            } else {
                getPresenter().modify(mBlogId, title, content);
            }
        });
    }

    @NonNull
    @Override
    protected EditContact.Presenter initPresenter() {
        return new EditPresenter();
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void setPublishEnable(boolean enable) {
        mBtnPublish.setEnabled(enable);
    }

    @Override
    public void showPublishingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mLoadingDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void dismissPublishingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isHidden()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void setBlog(Blog blog) {
        mEdtTitle.setText(blog.title);
        mEdtContent.setText(blog.content);
    }
}
