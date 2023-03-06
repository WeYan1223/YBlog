package com.cn.yblog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.yblog.R;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.util.ColorUtil;
import com.cn.yblog.util.TimeUtil;
import com.cn.yblog.widget.RoundHeader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {
    @IntDef
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        int NORMAL = 0x0001;
        int BIN = 0x0003;
        int FAVORITES = 0x0002;
        int PUBLISH = 0x0004;
    }

    private final List<Blog> mBlogList = new ArrayList<>();
    private final int mMode;
    private OnClickListener mOnClickListener;

    public BlogAdapter(@Mode int mode) {
        mMode = mode;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
        BlogViewHolder viewHolder = new BlogViewHolder(view);
        switch (mMode) {
            case Mode.NORMAL:
            case Mode.FAVORITES:
                viewHolder.imbMore.setVisibility(View.GONE);
                break;
            case Mode.PUBLISH:
            case Mode.BIN:
                viewHolder.imbFavorite.setVisibility(View.GONE);
                break;
            default:
                break;

        }
        view.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                mOnClickListener.onItemClick(position, mBlogList.get(position));
            }
        });
        viewHolder.imbFavorite.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                mOnClickListener.onFavoriteClick(position, mBlogList.get(position));
            }
        });
        viewHolder.imbMore.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                mOnClickListener.onMoreClick(position, mBlogList.get(position), viewHolder.imbMore);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = mBlogList.get(position);
        holder.roundHeader.setName(String.valueOf(blog.authorName.charAt(0)));
        holder.roundHeader.setBackgroundTint(ColorUtil.getColor(position));
        holder.tvPublishTime.setText(TimeUtil.getFormatTime(blog.publishTime));
        holder.tvAuthorName.setText(blog.authorName);
        holder.tvTitle.setText(blog.title);
        holder.imbFavorite.setActivated(blog.isFavorite);
    }

    @Override
    public int getItemCount() {
        return mBlogList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    /**
     * 收藏或取消收藏
     *
     * @param favor    true为收藏
     * @param position 博客下标
     */
    public void favorite(boolean favor, int position) {
        Blog blog = mBlogList.get(position);
        blog.isFavorite = favor;
        notifyItemChanged(position);
    }

    /**
     * 删除博客
     *
     * @param position 博客下标
     */
    public void delete(int position) {
        mBlogList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 刷新博客
     *
     * @param blogs blogs
     */
    public void replaceAll(List<Blog> blogs) {
        int count = mBlogList.size();
        mBlogList.clear();
        notifyItemRangeRemoved(0, count);
        mBlogList.addAll(blogs);
        notifyItemRangeInserted(0, blogs.size());
    }

    /**
     * 清空博客
     */
    public void removeAll() {
        int count = mBlogList.size();
        mBlogList.clear();
        notifyItemRangeRemoved(0, count);
    }

    /**
     * 将博客数据插入到尾部
     *
     * @param blogs blogs
     */
    public void insertEnd(List<Blog> blogs) {
        int positionStart = mBlogList.size() - 1;
        mBlogList.addAll(blogs);
        notifyItemRangeInserted(positionStart, blogs.size());
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        RoundHeader roundHeader;
        TextView tvPublishTime;
        TextView tvAuthorName;
        TextView tvTitle;
        ImageButton imbMore;
        ImageButton imbFavorite;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            roundHeader = itemView.findViewById(R.id.item_blog_round_header);
            tvPublishTime = itemView.findViewById(R.id.item_blog_tv_publish_time);
            tvAuthorName = itemView.findViewById(R.id.item_blog_tv_author_name);
            tvTitle = itemView.findViewById(R.id.item_blog_tv_title);
            imbMore = itemView.findViewById(R.id.item_blog_imb_more);
            imbFavorite = itemView.findViewById(R.id.item_blog_imb_favorite);
        }
    }

    public interface OnClickListener {
        /**
         * 点击整个数据项
         */
        void onItemClick(int position, Blog blog);

        /**
         * 点击收藏按钮
         */
        void onFavoriteClick(int position, Blog blog);

        /**
         * 点击更多按钮
         */
        void onMoreClick(int position, Blog blog, View view);
    }
}
