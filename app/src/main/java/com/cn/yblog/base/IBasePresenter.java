package com.cn.yblog.base;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public interface IBasePresenter<V extends IBaseView> {
    /**
     * 与View层进行绑定
     *
     * @param view {@link IBaseView}的子类
     */
    void bindView(V view);

    /**
     * 与View层进行解绑
     */
    void unbindView();

    /**
     * 判断是否已经绑定View层
     *
     * @return true 已绑定，否则返回false
     */
    boolean isViewBinded();

    /**
     * 获取View层的引用
     *
     * @return View层的引用，若未绑定View层则返回null
     */
    V getView();
}
