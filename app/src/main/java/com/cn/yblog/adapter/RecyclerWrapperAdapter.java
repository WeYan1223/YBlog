package com.cn.yblog.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * description: 使用装饰者模式为RecyclerView添加HeaderView与FooterView
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class RecyclerWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final RecyclerView.Adapter mInnerAdapter;
    private View mHeaderView;
    private View mFooterView;

    @IntDef({ItemType.TYPE_HEADER, ItemType.TYPE_FOOTER, ItemType.TYPE_NORMAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface ItemType {
        int TYPE_HEADER = 0x0001;
        int TYPE_FOOTER = 0x0002;
        int TYPE_NORMAL = 0x0003;
    }

    public RecyclerWrapperAdapter(RecyclerView.Adapter innerAdapter) {
        mInnerAdapter = innerAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == ItemType.TYPE_HEADER) {
            return new WrapperViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == ItemType.TYPE_FOOTER) {
            return new WrapperViewHolder(mFooterView);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ItemType.TYPE_HEADER) {
            return;
        } else if (getItemViewType(position) == ItemType.TYPE_FOOTER) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + getHeaderCount() + getFooterCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return ItemType.TYPE_NORMAL;
        }
        if (position == 0 && mHeaderView != null) {
            return ItemType.TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && mFooterView != null) {
            return ItemType.TYPE_FOOTER;
        }
        return ItemType.TYPE_NORMAL;
    }

    /**
     * HeaderView的数量
     *
     * @return 0或1
     */
    public int getHeaderCount() {
        return mHeaderView == null ? 0 : 1;
    }

    /**
     * HeaderView的数量的数量
     *
     * @return 0或1
     */
    public int getFooterCount() {
        return mFooterView == null ? 0 : 1;
    }

    /**
     * 获取除了HeaderView与HeaderView的数据项数量
     *
     * @return 真正的数据项数量
     */
    public int getRealCount() {
        return mInnerAdapter.getItemCount();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeHeaderView() {
        mHeaderView = null;
    }

    public void removeFooterView() {
        mFooterView = null;
    }

    private static class WrapperViewHolder extends RecyclerView.ViewHolder {
        WrapperViewHolder(View itemView) {
            super(itemView);
        }
    }
}
