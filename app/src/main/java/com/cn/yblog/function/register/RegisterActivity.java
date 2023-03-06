package com.cn.yblog.function.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.yblog.R;
import com.cn.yblog.base.BaseActivity;
import com.cn.yblog.dialog.LoadingDialog;
import com.cn.yblog.util.ToastUtil;
import com.cn.yblog.widget.TopBar;
import com.google.android.material.textfield.TextInputLayout;

/**
 * description: 注册界面
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/24
 * <p>version: 1.0
 * <p>update: none
 */
public class RegisterActivity extends BaseActivity<RegisterContact.Presenter> implements RegisterContact.View {
    private TopBar mTopBar;
    private EditText mEdtUsername;
    private EditText mEdtPassword;
    private EditText mEdtRePassword;
    private Button mBtnRegister;

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
    }

    private void initView() {
        mTopBar = findViewById(R.id.register_top_bar);
        mEdtUsername = ((TextInputLayout) findViewById(R.id.register_til_username)).getEditText();
        mEdtPassword = ((TextInputLayout) findViewById(R.id.register_til_password)).getEditText();
        mEdtRePassword = ((TextInputLayout) findViewById(R.id.register_til_re_password)).getEditText();
        mBtnRegister = findViewById(R.id.register_btn_register);
    }

    private void initListener() {
        mTopBar.setOnImbClickListener(new TopBar.OnImbClickListener() {
            @Override
            public void onLeftImbClick() {
                finish();
            }

            @Override
            public void onRightImbClick() {

            }
        });

        mBtnRegister.setOnClickListener(v -> {
            String username = mEdtUsername.getText().toString();
            String password = mEdtPassword.getText().toString();
            String rePassword = mEdtRePassword.getText().toString();
            getPresenter().register(username, password, rePassword);
        });
    }

    @NonNull
    @Override
    protected RegisterContact.Presenter initPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void showNormal(String normalMsg) {
        ToastUtil.show(normalMsg);
    }

    @Override
    public void showError(String errorMsg) {
        ToastUtil.showError(errorMsg);
    }

    @Override
    public void showLogonDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mLoadingDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void dismissLogonDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isHidden()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void backToLogin(String username, String password) {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }

}
