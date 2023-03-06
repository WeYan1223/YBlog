package com.cn.yblog.function.blog;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.cn.yblog.R;
import com.cn.yblog.base.BasePresenter;
import com.cn.yblog.data.local.sp.SpHelper;
import com.cn.yblog.data.remote.db.DaoBlog;
import com.cn.yblog.data.remote.db.DaoComment;
import com.cn.yblog.data.remote.db.GaussHelper;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.entity.Comment;
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
 * <p>time: 2023/1/1
 * <p>version: 1.0
 * <p>update: none
 */
public class BlogPresenter extends BasePresenter<BlogContact.View> implements BlogContact.Presenter {
    private final int mBlogId;
    private final Handler mHandler;
    private final DaoBlog mDaoBlog;
    private final DaoComment mDaoComment;

    public BlogPresenter(int blogId) {
        mBlogId = blogId;
        mHandler = new Handler(Looper.getMainLooper());
        mDaoBlog = GaussHelper.getInstance().getDaoBlog();
        mDaoComment = GaussHelper.getInstance().getDaoComment();
    }

    @Override
    public void loadBlog() {
        Runnable r = () -> {
            Blog blog;
            if ((blog = mDaoBlog.getBlog(mBlogId)) != null) {
                mHandler.post(() -> getView().setBlog(blog));
            }
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            ToastUtil.showError(R.string.error_request_timeout);
        }));
    }

    @Override
    public void loadComments() {
        Runnable r = () -> {
            List<Comment> comments;
            if ((comments = mDaoComment.getComments(mBlogId)) != null) {
                mHandler.post(() -> getView().setComments(comments));
            }
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            ToastUtil.showError(R.string.error_request_timeout);
        }));
    }

    @Override
    public void addComment(String content) {
        Runnable r = () -> {
            Comment comment = new Comment();
            comment.content = content;
            comment.userId = (int) SpHelper.get(SpHelper.Key.KEY_USER_ID, -1);
            comment.blogId = mBlogId;
            comment.time = TimeUtil.getCurMills();
            BlogChain chain = new BlogChain();
            chain.addHandler(new CheckEmptyHandler());
            chain.addHandler(new CommentHandler());
            Response<Boolean> response = chain.process(comment);
            if (!response.getData()) {
                mHandler.post(() -> ToastUtil.showError(response.getMsg()));
            } else {
                mHandler.post(() -> {
                    ToastUtil.show(response.getMsg());
                    getView().hideCommentDialog();
                    loadComments();
                });
            }
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            ToastUtil.showError(R.string.error_request_timeout);
        }));
    }

    private interface InnerHandler {
        /**
         * 责任链模式的抽象处理者
         *
         * @param comment 评论
         * @return data为true说明可以交给下一个处理者
         */
        @NonNull
        Response<Boolean> doHandler(Comment comment);
    }

    /**
     * 责任链，组合具体处理者
     */
    private static class BlogChain {
        private final List<InnerHandler> mInnerHandlerList = new ArrayList<>();

        public void addHandler(InnerHandler innerHandler) {
            mInnerHandlerList.add(innerHandler);
        }

        public Response<Boolean> process(Comment comment) {
            Response<Boolean> response = null;
            for (InnerHandler innerHandler : mInnerHandlerList) {
                response = innerHandler.doHandler(comment);
                if (!response.getData()) {
                    return response;
                }
            }
            return response;
        }
    }

    private class CheckEmptyHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(Comment comment) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setData(true);
            if (TextUtil.isEmpty(comment.content)) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.blog_error_empty_content));
            }
            return response;
        }
    }

    private class CommentHandler implements InnerHandler {
        @NonNull
        @Override
        public Response<Boolean> doHandler(Comment comment) {
            Response<Boolean> response = new Response<>();
            response.setCode(Response.Code.CODE_SUCCESS);
            response.setMsg(AppUtil.getString(R.string.blog_normal_comment_success));
            response.setData(true);
            if (mDaoComment.insert(comment) == 0) {
                response.setCode(Response.Code.CODE_FAILURE);
                response.setData(false);
                response.setMsg(AppUtil.getString(R.string.blog_error_comment_failure));
            }
            return response;
        }
    }
}
