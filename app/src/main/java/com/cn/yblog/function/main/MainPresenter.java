package com.cn.yblog.function.main;

import android.os.Handler;
import android.os.Looper;

import com.cn.yblog.R;
import com.cn.yblog.base.BasePresenter;
import com.cn.yblog.data.local.sp.SpHelper;
import com.cn.yblog.data.remote.db.DaoBlog;
import com.cn.yblog.data.remote.db.DaoFavorites;
import com.cn.yblog.data.remote.db.GaussHelper;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.service.RemoteService;
import com.cn.yblog.util.AppUtil;
import com.cn.yblog.util.TimeUtil;
import com.cn.yblog.util.ToastUtil;

import java.util.List;


/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/24
 * <p>version: 1.0
 * <p>update: none
 */
public class MainPresenter extends BasePresenter<MainContact.View> implements MainContact.Presenter {
    private final Handler mHandler;
    private final DaoBlog mDaoBlog;
    private final DaoFavorites mDaoFavorites;

    public MainPresenter() {
        mHandler = new Handler(Looper.getMainLooper());
        mDaoBlog = GaussHelper.getInstance().getDaoBlog();
        mDaoFavorites = GaussHelper.getInstance().getDaoFavorites();
    }

    @Override
    public void refresh() {
        getView().showRefreshView();
        Runnable r = () -> {
            int userId = (int) SpHelper.get(SpHelper.Key.KEY_USER_ID, -1);
            List<Blog> blogs = mDaoBlog.getBlogs(userId, 0);
            mHandler.post(() -> {
                getView().hideRefreshView();
                getView().replaceAll(blogs);
                ToastUtil.show(R.string.normal_refresh_success);
            });
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            getView().hideRefreshView();
            ToastUtil.showError(AppUtil.getString(R.string.error_request_timeout));
        }));
    }

    @Override
    public void loadMore(int offset) {

    }

    @Override
    public void favorite(boolean favor, int blogId, int position) {
        RemoteService.getInstance().execute(() -> {
            int userId = (int) SpHelper.get(SpHelper.Key.KEY_USER_ID, -1);
            int result;
            if (favor) {
                result = mDaoFavorites.insert(userId, blogId, TimeUtil.getCurMills());
            } else {
                result = mDaoFavorites.delete(userId, blogId);
            }
            if (result == 1) {
                mHandler.post(() -> {
                    getView().favorite(favor, position);
                    ToastUtil.show(favor ? R.string.main_normal_favorite_success : R.string.main_normal_un_favorite_success);
                });
            }
        });
    }

    @Override
    public void logout() {
        SpHelper.clear();
        getView().toLogin();
    }
}
