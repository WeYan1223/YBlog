package com.cn.yblog.function.register;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.cn.yblog.R;
import com.cn.yblog.base.BasePresenter;
import com.cn.yblog.data.remote.db.GaussHelper;
import com.cn.yblog.data.remote.db.DaoUser;
import com.cn.yblog.entity.Response;
import com.cn.yblog.entity.User;
import com.cn.yblog.service.RemoteService;
import com.cn.yblog.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/24
 * <p>version: 1.0
 * <p>update: none
 */
public class RegisterPresenter extends BasePresenter<RegisterContact.View> implements RegisterContact.Presenter {
    private final Handler mHandler;
    private final DaoUser mDaoUser;

    public RegisterPresenter() {
        mHandler = new Handler(Looper.getMainLooper());
        mDaoUser = GaussHelper.getInstance().getDaoUser();
    }

    @Override
    public void register(String username, String password, String rePassword) {
//        getView().showLogonDialog();
        Runnable r = () -> {
            LoginChain chain = new LoginChain();
            chain.addHandler(new UsernameInnerHandler());
            chain.addHandler(new PasswordInnerHandler());
            chain.addHandler(new RePasswordInnerHandler());
            chain.addHandler(new VerificationInnerHandler());
            Response<Boolean> response = chain.process(username, password, rePassword);
            if (response.getData()) {
                mHandler.post(() -> {
                    getView().dismissLogonDialog();
                    getView().showNormal(response.getMsg() + " " + AppUtil.getString(R.string.register_normal_to_login_soon));
                });
                mHandler.postDelayed(() -> getView().backToLogin(username, password), 1000);
            } else {
                mHandler.post(() -> {
                    getView().dismissLogonDialog();
                    getView().showError(response.getMsg());
                });
            }
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            getView().dismissLogonDialog();
            getView().showError(AppUtil.getString(R.string.error_request_timeout));
        }));
    }

    private interface InnerHandler {
        /**
         * 责任链模式的抽象处理者
         *
         * @param username 用户名
         * @param password 密码
         * @return data为true说明可以交给下一个处理者
         */
        @NonNull
        Response<Boolean> doHandler(String username, String password, String rePassword);
    }

    /**
     * 责任链，组合具体处理者
     */
    private static class LoginChain {
        private final List<InnerHandler> mInnerHandlerList = new ArrayList<>();

        public void addHandler(InnerHandler innerHandler) {
            mInnerHandlerList.add(innerHandler);
        }

        public Response<Boolean> process(String username, String password, String rePassword) {
            Response<Boolean> response = null;
            for (InnerHandler innerHandler : mInnerHandlerList) {
                response = innerHandler.doHandler(username, password, rePassword);
                if (!response.getData()) {
                    return response;
                }
            }
            return response;
        }
    }

    /**
     * 用户名具体处理者
     */
    private static class UsernameInnerHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(String username, String password, String rePassword) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            if (TextUtils.isEmpty(username)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.register_error_empty_username));
            }
            return response;
        }
    }

    /**
     * 密码具体处理者
     */
    private class PasswordInnerHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(String username, String password, String rePassword) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            if (TextUtils.isEmpty(password)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.register_error_empty_password));
            }
            return response;
        }
    }

    /**
     * 密码具体处理者
     */
    private class RePasswordInnerHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(String username, String password, String rePassword) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            if (TextUtils.isEmpty(rePassword)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.register_error_empty_re_password));
            }
            if (!password.equals(rePassword)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.register_error_password_not_match));
            }
            return response;
        }
    }

    /**
     * 验证身份具体处理者
     */
    private class VerificationInnerHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(String username, String password, String rePassword) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            User user = mDaoUser.get(username);
            if (user != null) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.register_error_register_failure));
            } else {
                if (mDaoUser.insert(new User(-1, username, password)) == 1) {
                    response.setMsg(AppUtil.getString(R.string.register_normal_register_success));
                } else {
                    response.setCode(Response.Code.CODE_FAILURE);
                    response.setData(false);
                    response.setMsg(AppUtil.getString(R.string.register_error_register_failure_un_know));
                }
            }
            return response;
        }
    }
}
