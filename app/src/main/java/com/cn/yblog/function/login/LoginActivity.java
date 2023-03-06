package com.cn.yblog.function.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cn.yblog.R;
import com.cn.yblog.base.BaseActivity;
import com.cn.yblog.dialog.LoadingDialog;
import com.cn.yblog.function.main.MainActivity;
import com.cn.yblog.function.register.RegisterActivity;
import com.cn.yblog.util.ToastUtil;
import com.google.android.material.textfield.TextInputLayout;

/**
 * description: 登录界面
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class LoginActivity extends BaseActivity<LoginContact.Presenter> implements LoginContact.View {
    private static final int REQUEST_CODE_REGISTER = 0x001;

    private EditText mEdtUsername;
    private EditText mEdtPassword;
    private Button mBtnLogin;
    private Button mBtnRegister;

    private LoadingDialog mLoadingDialog;

    /**
     * 启动登录界面的唯一入口
     * <p>启动登录界面前，任务栈的所有Activity都会被弹出
     *
     * @param context 上下文
     */
    public static void startAction(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    private void initView() {
        mEdtUsername = ((TextInputLayout) findViewById(R.id.login_til_username)).getEditText();
        mEdtPassword = ((TextInputLayout) findViewById(R.id.login_til_password)).getEditText();
        mBtnLogin = findViewById(R.id.login_btn_login);
        mBtnRegister = findViewById(R.id.login_btn_register);
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(v -> {
            String username = mEdtUsername.getText().toString();
            String password = mEdtPassword.getText().toString();
            getPresenter().login(username, password);
        });

        mBtnRegister.setOnClickListener(v -> toRegister());
    }

    @NonNull
    @Override
    protected LoginContact.Presenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            if (data != null) {
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                mEdtUsername.setText(username);
                mEdtPassword.setText(password);
            }
        }
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
    public void showLoginDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mLoadingDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void dismissLoginDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isHidden()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }

    @Override
    public void toMain() {
        MainActivity.startAction(this);
    }
}
