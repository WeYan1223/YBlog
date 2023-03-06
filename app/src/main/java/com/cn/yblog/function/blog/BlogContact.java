package com.cn.yblog.function.blog;

import com.cn.yblog.base.IBasePresenter;
import com.cn.yblog.base.IBaseView;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.entity.Comment;

import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2023/1/1
 * <p>version: 1.0
 * <p>update: none
 */
public interface BlogContact {
    interface View extends IBaseView {
        /**
         * 设置博客
         *
         * @param blog 博客
         */
        void setBlog(Blog blog);

        /**
         * 设置评论
         *
         * @param comments 评论
         */
        void setComments(List<Comment> comments);

        /**
         * 展示评论对话框
         */
        void showCommentDialog();

        /**
         * 隐藏评论对话框
         */
        void hideCommentDialog();
    }

    interface Presenter extends IBasePresenter<View> {
        /**
         * 加载博客
         */
        void loadBlog();

        /**
         * 加载评论
         */
        void loadComments();

        /**
         * 添加评论
         *
         * @param content 评论内容
         */
        void addComment(String content);
    }
}
