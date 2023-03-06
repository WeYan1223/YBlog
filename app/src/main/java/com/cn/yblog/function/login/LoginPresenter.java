package com.cn.yblog.function.login;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.cn.yblog.R;
import com.cn.yblog.base.BasePresenter;
import com.cn.yblog.data.local.sp.SpHelper;
import com.cn.yblog.data.remote.db.GaussHelper;
import com.cn.yblog.data.remote.db.DaoUser;
import com.cn.yblog.entity.Response;
import com.cn.yblog.entity.User;
import com.cn.yblog.service.RemoteService;
import com.cn.yblog.util.AppUtil;
import com.cn.yblog.util.TextUtil;
import com.cn.yblog.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class LoginPresenter extends BasePresenter<LoginContact.View> implements LoginContact.Presenter {
    private final Handler mHandler;
    private final DaoUser mDaoUser;
    ViewGroup

    public LoginPresenter() {
        mHandler = new Handler(Looper.getMainLooper());
        mDaoUser = GaussHelper.getInstance().getDaoUser();
    }

    @Override
    public void login(String username, String password) {
//        getView().showLoginDialog();
        Runnable r = () -> {
            LoginChain chain = new LoginChain();
            chain.addHandler(new UsernameInnerHandler());
            chain.addHandler(new PasswordInnerHandler());
            chain.addHandler(new VerificationInnerHandler());
            Response<Boolean> response = chain.process(username, password);
            if (response.getData()) {
                mHandler.post(() -> {
                    getView().dismissLoginDialog();
                    getView().showNormal(response.getMsg());
                    getView().toMain();
                });
            } else {
                mHandler.post(() -> {
                    getView().dismissLoginDialog();
                    getView().showError(response.getMsg());
                });
            }
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            getView().dismissLoginDialog();
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
        Response<Boolean> doHandler(String username, String password);
    }

    /**
     * 责任链，组合具体处理者
     */
    private static class LoginChain {
        private final List<InnerHandler> mInnerHandlerList = new ArrayList<>();

        public void addHandler(InnerHandler innerHandler) {
            mInnerHandlerList.add(innerHandler);
        }

        public Response<Boolean> process(String username, String password) {
            Response<Boolean> response = null;
            for (InnerHandler innerHandler : mInnerHandlerList) {
                response = innerHandler.doHandler(username, password);
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
        public Response<Boolean> doHandler(String username, String password) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            if (TextUtil.isEmpty(username)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.login_error_empty_username));
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
        public Response<Boolean> doHandler(String username, String password) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            if (TextUtils.isEmpty(password)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.login_error_empty_password));
            }
            return response;
        }
    }

    /**
     * 验证身份具体处理者
     * <p>如果验证成功，会先将用户保存到sp中
     */
    private class VerificationInnerHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(String username, String password) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            User user = mDaoUser.get(username, password);
            if (user == null) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.login_error_login_failure));
            } else {
                response.setMsg(AppUtil.getString(R.string.login_normal_login_success));
                SpHelper.put(SpHelper.Key.KEY_USER_ID, user.id);
                SpHelper.put(SpHelper.Key.KEY_USERNAME, user.username);
                SpHelper.put(SpHelper.Key.KEY_PASSWORD, user.password);
                SpHelper.put(SpHelper.Key.KEY_LAST_LOGIN_TIME, TimeUtil.getCurMills());
            }
            return response;
        }
    }
}
