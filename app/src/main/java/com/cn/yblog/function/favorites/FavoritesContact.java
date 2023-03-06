package com.cn.yblog.function.favorites;

import com.cn.yblog.base.IBasePresenter;
import com.cn.yblog.base.IBaseView;
import com.cn.yblog.entity.Blog;

import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/26
 * <p>version: 1.0
 * <p>update: none
 */
public interface FavoritesContact {
    interface View extends IBaseView {
        /**
         * 刷新
         *
         * @param blogs 最新的博客
         */
        void replaceAll(List<Blog> blogs);

        /**
         * 收藏或取消收藏
         *
         * @param favor    true为收藏
         * @param position 博客下标
         */
        void favorite(boolean favor, int position);

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
         * 收藏或取消收藏
         *
         * @param favor    true为收藏
         * @param blogId   博客ID
         * @param position 博客下标
         */
        void favorite(boolean favor, int blogId, int position);
    }
}
