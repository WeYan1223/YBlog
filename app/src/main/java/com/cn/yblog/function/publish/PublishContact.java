package com.cn.yblog.function.publish;

import com.cn.yblog.base.IBasePresenter;
import com.cn.yblog.base.IBaseView;
import com.cn.yblog.entity.Blog;

import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/27
 * <p>version: 1.0
 * <p>update: none
 */
public interface PublishContact {
    interface View extends IBaseView {
        /**
         * 刷新
         *
         * @param blogs 最新的博客
         */
        void replaceAll(List<Blog> blogs);

        /**
         * 删除
         *
         * @param position 博客下标
         */
        void delete(int position);

        /**
         * 显示刷新控件
         */
        void showRefreshView();

        /**
         * 隐藏刷新控件
         */
        void hideRefreshView();
    }

    interface Presenter extends IBasePresenter<View> {
        /**
         * 刷新博客
         */
        void refresh();

        /**
         * 删除
         *
         * @param blogId   博客ID
         * @param position 博客下标
         */
        void delete(int blogId, int position);
    }
}
