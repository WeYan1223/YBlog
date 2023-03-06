package com.cn.yblog.base;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    private V mView;

    @Override
    public void bindView(V view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    @Override
    public boolean isViewBinded() {
        return mView != null;
    }

    @Override
    public V getView() {
        return mView;
    }
}
