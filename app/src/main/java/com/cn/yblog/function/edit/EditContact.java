package com.cn.yblog.function.edit;

import com.cn.yblog.base.IBasePresenter;
import com.cn.yblog.base.IBaseView;
import com.cn.yblog.entity.Blog;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public interface EditContact {
    interface View extends IBaseView {
        /**
         * 返回
         */
        void back();

        /**
         * 设置发布键是否可用
         *
         * @param enable true为可用
         */
        void setPublishEnable(boolean enable);

        /**
         * 显示发布加载框
         */
        void showPublishingDialog();

        /**
         * 隐藏发布加载框
         */
        void dismissPublishingDialog();

        /**
         * 设置博客内容
         *
         * @param blog 博客
         */
        void setBlog(Blog blog);

    }

    interface Presenter extends IBasePresenter<View> {
        /**
         * 加载博客信息
         *
         * @param blogId 博客ID
         */
        void loadBlog(int blogId);

        /**
         * 发布博客
         *
         * @param title   标题
         * @param content 内容
         */
        void publish(String title, String content);

        /**
         * 修改博客
         *
         * @param blogId  博客原ID
         * @param title   修改后的标题
         * @param content 修改后的内容
         */
        void modify(int blogId, String title, String content);
    }
}
