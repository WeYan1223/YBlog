package com.cn.yblog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.yblog.R;
import com.cn.yblog.entity.Comment;
import com.cn.yblog.util.ColorUtil;
import com.cn.yblog.util.TimeUtil;
import com.cn.yblog.widget.RoundHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2023/1/2
 * <p>version: 1.0
 * <p>update: none
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final List<Comment> mCommentList = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        holder.roundHeader.setName(String.valueOf(comment.userName.charAt(0)));
        holder.roundHeader.setBackgroundTint(ColorUtil.getColor(position));
        holder.tvUsername.setText(comment.userName);
        holder.tvContent.setText(comment.content);
        holder.tvTime.setText(TimeUtil.getFormatTime(comment.time));
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    /**
     * 刷新博客
     *
     * @param comments comments
     */
    public void replaceAll(List<Comment> comments) {
        int count = mCommentList.size();
        mCommentList.clear();
        notifyItemRangeRemoved(0, count);
        mCommentList.addAll(comments);
        notifyItemRangeInserted(0, comments.size());
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        RoundHeader roundHeader;
        TextView tvUsername;
        TextView tvContent;
        TextView tvTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            roundHeader = itemView.findViewById(R.id.item_comment_round_header);
            tvUsername = itemView.findViewById(R.id.item_comment_tv_user_name);
            tvContent = itemView.findViewById(R.id.item_comment_tv_content);
            tvTime = itemView.findViewById(R.id.item_comment_tv_time);
        }
    }
}
