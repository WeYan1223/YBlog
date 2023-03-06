package com.cn.yblog.function.bin;

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
import com.cn.yblog.widget.EmptyRecyclerView;
import com.cn.yblog.widget.EmptyView;
import com.cn.yblog.widget.TopBar;

import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/26
 * <p>version: 1.0
 * <p>update: none
 */
public class BinActivity extends BaseActivity<BinContact.Presenter> implements BinContact.View {
    private TopBar mTopBar;
    private SwipeRefreshLayout mRefreshView;
    private EmptyRecyclerView mRvBlogs;

    private BlogAdapter mBlogAdapter;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, BinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);
        initView();
        initListener();
        showRefreshView();
        new Handler(Looper.getMainLooper()).postDelayed(() -> getPresenter().refresh(), 1000);
    }

    @NonNull
    @Override
    protected BinContact.Presenter initPresenter() {
        return new BinPresenter();
    }

    private void initView() {
        mTopBar = findViewById(R.id.bin_top_bar);
        mRefreshView = findViewById(R.id.bin_refresh_blogs);
        mRvBlogs = findViewById(R.id.bin_rv_blogs);

        mBlogAdapter = new BlogAdapter(BlogAdapter.Mode.BIN);
        mRvBlogs.setLayoutManager(new LinearLayoutManager(this));
        mRvBlogs.setAdapter(mBlogAdapter);
        EmptyView emptyView = findViewById(R.id.bin_empty_view);
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
                getPresenter().clear();
            }
        });

        mBlogAdapter.setOnClickListener(new BlogAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position, Blog blog) {
                BlogActivity.startAction(BinActivity.this, blog);
            }

            @Override
            public void onFavoriteClick(int position, Blog blog) {

            }

            @Override
            public void onMoreClick(int position, Blog blog, View view) {
                PopupMenu popupMenu = new PopupMenu(BinActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_bin_action, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
                    switch (item.getOrder()) {
                        case 0:
                            getPresenter().recovery(blog, position);
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
    public void removeAll() {
        mBlogAdapter.removeAll();
    }

    @Override
    public void recovery(int position) {
        mBlogAdapter.delete(position);
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
