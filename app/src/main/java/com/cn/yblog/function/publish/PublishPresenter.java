package com.cn.yblog.function.publish;

import android.os.Handler;
import android.os.Looper;

import com.cn.yblog.R;
import com.cn.yblog.base.BasePresenter;
import com.cn.yblog.data.local.sp.SpHelper;
import com.cn.yblog.data.remote.db.DaoBlog;
import com.cn.yblog.data.remote.db.GaussHelper;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.service.RemoteService;
import com.cn.yblog.util.AppUtil;
import com.cn.yblog.util.ToastUtil;

import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/27
 * <p>version: 1.0
 * <p>update: none
 */
public class PublishPresenter extends BasePresenter<PublishContact.View> implements PublishContact.Presenter {
    private final Handler mHandler;
    private final DaoBlog mDaoBlog;

    public PublishPresenter() {
        mHandler = new Handler(Looper.getMainLooper());
        mDaoBlog = GaussHelper.getInstance().getDaoBlog();
    }

    @Override
    public void refresh() {
        getView().showRefreshView();
        Runnable r = () -> {
            int userId = (int) SpHelper.get(SpHelper.Key.KEY_USER_ID, -1);
            List<Blog> blogs = mDaoBlog.getBlogs(userId);
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
    public void delete(int blogId, int position) {
        Runnable r = () -> {
            int result = mDaoBlog.delete(blogId);
            if (result == 1) {
                mHandler.post(() -> {
                    getView().delete(position);
                    ToastUtil.show(R.string.publish_normal_delete_success);
                });
            }
        };
        RemoteService.getInstance().submitWithTimeout(r, 10, () -> mHandler.post(() -> {
            ToastUtil.showError(AppUtil.getString(R.string.error_request_timeout));
        }));
    }
}
