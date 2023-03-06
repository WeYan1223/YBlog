package com.cn.yblog.function.edit;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.cn.yblog.R;
import com.cn.yblog.base.BasePresenter;
import com.cn.yblog.data.local.sp.SpHelper;
import com.cn.yblog.data.remote.db.DaoBlog;
import com.cn.yblog.data.remote.db.GaussHelper;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.entity.Response;
import com.cn.yblog.service.RemoteService;
import com.cn.yblog.util.AppUtil;
import com.cn.yblog.util.TextUtil;
import com.cn.yblog.util.TimeUtil;
import com.cn.yblog.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class EditPresenter extends BasePresenter<EditContact.View> implements EditContact.Presenter {
    private final Handler mHandler;
    private final DaoBlog mDaoBlog;

    public EditPresenter() {
        mHandler = new Handler(Looper.getMainLooper());
        mDaoBlog = GaussHelper.getInstance().getDaoBlog();
    }

    @Override
    public void loadBlog(int blogId) {
        Runnable r = () -> {
            Blog blog;
            if ((blog = mDaoBlog.getBlog(blogId)) != null) {
                mHandler.post(() -> getView().setBlog(blog));
            }
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            ToastUtil.showError(R.string.error_request_timeout);
        }));
    }

    @Override
    public void publish(String title, String content) {
        getView().setPublishEnable(false);
        getView().showPublishingDialog();

        Runnable r = () -> {
            Blog blog = new Blog();
            blog.title = title;
            blog.content = content;
            blog.authorId = (int) SpHelper.get(SpHelper.Key.KEY_USER_ID, -1);
            blog.authorName = (String) SpHelper.get(SpHelper.Key.KEY_USERNAME, "");
            blog.publishTime = TimeUtil.getCurMills();

            EditChain chain = new EditChain();
            chain.addHandler(new CheckEmptyHandler());
            chain.addHandler(new PublishHandler());
            Response<Boolean> response = chain.process(blog);

            if (!response.getData()) {
                mHandler.post(() -> {
                    getView().setPublishEnable(true);
                    getView().dismissPublishingDialog();
                    ToastUtil.showError(response.getMsg());
                });
            } else {
                mHandler.post(() -> {
                    getView().dismissPublishingDialog();
                    ToastUtil.show(response.getMsg());
                });
                mHandler.postDelayed(() -> getView().back(), 1500);
            }
        };

        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            getView().setPublishEnable(true);
            getView().dismissPublishingDialog();
            ToastUtil.showError(R.string.error_request_timeout);
        }));
    }

    @Override
    public void modify(int blogId, String title, String content) {
        getView().setPublishEnable(false);
        getView().showPublishingDialog();

        Runnable r = () -> {
            Blog blog = mDaoBlog.getBlog(blogId);
            blog.title = title;
            blog.content = content;

            EditChain chain = new EditChain();
            chain.addHandler(new CheckEmptyHandler());
            chain.addHandler(new ModifyHandler());
            Response<Boolean> response = chain.process(blog);

            if (!response.getData()) {
                mHandler.post(() -> {
                    getView().setPublishEnable(true);
                    getView().dismissPublishingDialog();
                    ToastUtil.showError(response.getMsg());
                });
            } else {
                mHandler.post(() -> {
                    getView().dismissPublishingDialog();
                    ToastUtil.show(response.getMsg());
                });
                mHandler.postDelayed(() -> getView().back(), 1500);
            }
        };

        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            getView().setPublishEnable(true);
            getView().dismissPublishingDialog();
            ToastUtil.showError(R.string.error_request_timeout);
        }));
    }

    private interface InnerHandler {
        /**
         * 责任链模式的抽象处理者
         *
         * @param blog 博客
         * @return data为true说明可以交给下一个处理者
         */
        @NonNull
        Response<Boolean> doHandler(Blog blog);
    }

    /**
     * 责任链，组合具体处理者
     */
    private static class EditChain {
        private final List<InnerHandler> mInnerHandlerList = new ArrayList<>();

        public void addHandler(InnerHandler innerHandler) {
            mInnerHandlerList.add(innerHandler);
        }

        public Response<Boolean> process(Blog blog) {
            Response<Boolean> response = null;
            for (InnerHandler innerHandler : mInnerHandlerList) {
                response = innerHandler.doHandler(blog);
                if (!response.getData()) {
                    return response;
                }
            }
            return response;
        }
    }

    /**
     * 检查输入是否为空具体处理者
     */
    private class CheckEmptyHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(Blog blog) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            if (TextUtil.isEmpty(blog.title)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.edit_error_empty_title));
            } else if (TextUtil.isEmpty(blog.content)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.edit_error_empty_content));
            }
            return response;
        }
    }

    /**
     * 发布博客具体处理者
     */
    private class PublishHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(Blog blog) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            response.setMsg(AppUtil.getString(R.string.edit_normal_publish_success));
            if (mDaoBlog.insert(blog) == 0) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.edit_error_publish_failure));
            }
            return response;
        }
    }

    /**
     * 修改博客具体处理者
     */
    private class ModifyHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(Blog blog) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            response.setMsg(AppUtil.getString(R.string.edit_normal_modify_success));
            if (mDaoBlog.update(blog) == 0) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.edit_error_modify_failure));
            }
            return response;
        }
    }
}
