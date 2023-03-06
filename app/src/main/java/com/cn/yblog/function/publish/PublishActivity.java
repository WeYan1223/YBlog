package com.cn.yblog.function.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cn.yblog.R;
import com.cn.yblog.adapter.BlogAdapter;
import com.cn.yblog.base.BaseActivity;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.function.blog.BlogActivity;
import com.cn.yblog.function.edit.EditActivity;
import com.cn.yblog.widget.EmptyRecyclerView;
import com.cn.yblog.widget.EmptyView;
import com.cn.yblog.widget.TopBar;

import java.util.List;

/**
 * description: 我的发布界面
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/26
 * <p>version: 1.0
 * <p>update: none
 */
public class PublishActivity extends BaseActivity<PublishContact.Presenter> implements PublishContact.View {
    private TopBar mTopBar;
    private SwipeRefreshLayout mRefreshView;
    private EmptyRecyclerView mRvBlogs;

    private BlogAdapter mBlogAdapter;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initListener();
        showRefreshView();
        new Handler(Looper.getMainLooper()).postDelayed(() -> getPresenter().refresh(), 1000);
    }


    @NonNull
    @Override
    protected PublishContact.Presenter initPresenter() {
        return new PublishPresenter();
    }

    private void initView() {
        mTopBar = findViewById(R.id.publish_top_bar);
        mRefreshView = findViewById(R.id.publish_refresh_blogs);
        mRvBlogs = findViewById(R.id.publish_rv_blogs);

        mBlogAdapter = new BlogAdapter(BlogAdapter.Mode.PUBLISH);
        mRvBlogs.setLayoutManager(new LinearLayoutManager(this));
        mRvBlogs.setAdapter(mBlogAdapter);
        EmptyView emptyView = findViewById(R.id.publish_empty_view);
        mRvBlogs.setEmptyView(emptyView);
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

        mBlogAdapter.setOnClickListener(new BlogAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position, Blog blog) {
                BlogActivity.startAction(PublishActivity.this, blog);
            }

            @Override
            public void onFavoriteClick(int position, Blog blog) {

            }

            @Override
            public void onMoreClick(int position, Blog blog, View view) {
                PopupMenu popupMenu = new PopupMenu(PublishActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_publish_action, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
                    switch (item.getOrder()) {
                        case 0:
                            EditActivity.startAction(PublishActivity.this, EditActivity.Mode.MODIFY, blog.id);
                            break;
                        case 1:
                            getPresenter().delete(blog.id, position);
                            break;
                        default:
                            break;
                    }
                    return true;
                });
                popupMenu.show();
            }
        });

        mRefreshView.setOnRefreshListener(() -> getPresenter().refresh());
    }

    @Override
    public void replaceAll(List<Blog> blogs) {
        mBlogAdapter.replaceAll(blogs);
    }

    @Override
    public void delete(int position) {
        mBlogAdapter.delete(position);
    }

    @Override
    public void showRefreshView() {
        if (!mRefreshView.isRefreshing()) {
            mRefreshView.setRefreshing(true);
        }
    }

    @Override
    public void hideRefreshView() {
        if (mRefreshView.isRefreshing()) {
            mRefreshView.setRefreshing(false);
        }
    }
}
