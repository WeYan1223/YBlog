package com.cn.yblog.function.blog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.yblog.R;
import com.cn.yblog.adapter.CommentAdapter;
import com.cn.yblog.base.BaseActivity;
import com.cn.yblog.dialog.CommentDialog;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.entity.Comment;
import com.cn.yblog.util.AppUtil;
import com.cn.yblog.util.TimeUtil;
import com.cn.yblog.widget.RoundHeader;
import com.cn.yblog.widget.TopBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

/**
 * description: 博客界面（不可编辑）
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/26
 * <p>version: 1.0
 * <p>update: none
 */
public class BlogActivity extends BaseActivity<BlogContact.Presenter> implements BlogContact.View {
    private TopBar mTopBar;
    private RoundHeader mHeader;
    private TextView mTvPublishTime;
    private TextView mTvAuthorName;
    private TextView mTvContent;
    private TextView mTvComment;
    private RecyclerView mRvComments;
    private CommentAdapter mCommentAdapter;
    private ExtendedFloatingActionButton mFabComment;

    private CommentDialog mCommentDialog;

    /**
     * 博客界面的唯一入口
     *
     * @param context 上下文
     * @param blog    博客
     */
    public static void startAction(Context context, Blog blog) {
        Intent intent = new Intent(context, BlogActivity.class);
        intent.putExtra("blogId", blog.id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        initView();
        initListener();
        getPresenter().loadBlog();
        getPresenter().loadComments();
    }

    @NonNull
    @Override
    protected BlogContact.Presenter initPresenter() {
        Intent intent = getIntent();
        int blogId = intent.getIntExtra("blogId", -1);
        return new BlogPresenter(blogId);
    }

    private void initView() {
        mTopBar = findViewById(R.id.blog_top_bar);
        mHeader = findViewById(R.id.blog_round_header);
        mTvPublishTime = findViewById(R.id.blog_tv_publish_time);
        mTvAuthorName = findViewById(R.id.blog_tv_author_name);
        mTvContent = findViewById(R.id.blog_tv_content);
        mTvComment = findViewById(R.id.blog_tv_comment);
        mRvComments = findViewById(R.id.blog_rv_comments);
        mFabComment = findViewById(R.id.blog_fab_comment);
        mCommentAdapter = new CommentAdapter();
        mRvComments.setLayoutManager(new LinearLayoutManager(this));
        mRvComments.setAdapter(mCommentAdapter);
    }

    private void initListener() {
        mTopBar.setOnImbClickListener(new TopBar.OnImbClickListener() {
            @Override
            public void onLeftImbClick() {
                finish();
            }

            @Override
            public void onRightImbClick() {

            }
        });

        mFabComment.setOnClickListener(v -> showCommentDialog());
    }

    @Override
    public void setBlog(Blog blog) {
        mTopBar.setTitle(blog.title);
        mHeader.setBackgroundTint(getResources().getColor(R.color.blue_400));
        mHeader.setName(String.valueOf(blog.authorName.charAt(0)));
        mTvPublishTime.setText(TimeUtil.getFormatTime(blog.publishTime));
        mTvAuthorName.setText(blog.authorName);
        mTvContent.setText(blog.content);
    }

    @Override
    public void setComments(List<Comment> comments) {
        mTvComment.setText(String.format(AppUtil.getString(R.string.blog_normal_comment_counts), comments.size()));
        mCommentAdapter.replaceAll(comments);
    }

    @Override
    public void showCommentDialog() {
        if (mCommentDialog == null) {
            mCommentDialog = new CommentDialog();
            mCommentDialog.setOnCommentListener(comment -> getPresenter().addComment(comment));
        }
        mCommentDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void hideCommentDialog() {
        if (!mCommentDialog.isHidden()) {
            mCommentDialog.dismiss();
        }
    }
}
