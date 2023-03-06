package com.cn.yblog.function.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cn.yblog.R;
import com.cn.yblog.adapter.BlogAdapter;
import com.cn.yblog.adapter.RecyclerWrapperAdapter;
import com.cn.yblog.base.BaseActivity;
import com.cn.yblog.data.local.sp.SpHelper;
import com.cn.yblog.entity.Blog;
import com.cn.yblog.function.bin.BinActivity;
import com.cn.yblog.function.blog.BlogActivity;
import com.cn.yblog.function.edit.EditActivity;
import com.cn.yblog.function.favorites.FavoritesActivity;
import com.cn.yblog.function.login.LoginActivity;
import com.cn.yblog.function.publish.PublishActivity;
import com.cn.yblog.util.AppUtil;
import com.cn.yblog.util.ButtonUtil;
import com.cn.yblog.widget.EmptyRecyclerView;
import com.cn.yblog.widget.EmptyView;
import com.cn.yblog.widget.RoundHeader;
import com.cn.yblog.widget.TopBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * description: 主界面
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class MainActivity extends BaseActivity<MainContact.Presenter> implements MainContact.View {
    private TopBar mTopBar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private FloatingActionButton mFabAdd;
    private SwipeRefreshLayout mRefreshView;
    private EmptyRecyclerView mRvBlogs;

    private BlogAdapter mBlogAdapter;
    private RecyclerWrapperAdapter mWrapperAdapter;

    /**
     * 启动主界面的唯一入口
     * <p>启动主界面前，任务栈的所有Activity都会被弹出
     *
     * @param context 上下文
     */
    public static void startAction(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();

        showRefreshView();
        new Handler(Looper.getMainLooper()).postDelayed(() -> getPresenter().refresh(), 1000);
    }

    @NonNull
    @Override
    protected MainContact.Presenter initPresenter() {
        return new MainPresenter();
    }


    private void initView() {
        mTopBar = findViewById(R.id.main_top_bar);
        mDrawer = findViewById(R.id.main_drawer);
        mNavigationView = findViewById(R.id.main_nav_view);
        mFabAdd = findViewById(R.id.main_fab_add);
        mRefreshView = findViewById(R.id.main_refresh_blogs);
        mRvBlogs = findViewById(R.id.main_rv_blogs);

        mBlogAdapter = new BlogAdapter(BlogAdapter.Mode.NORMAL);
//        mWrapperAdapter = new RecyclerWrapperAdapter(mBlogAdapter);
        mRvBlogs.setAdapter(mBlogAdapter);
        mRvBlogs.setLayoutManager(new LinearLayoutManager(this));
        EmptyView emptyView = findViewById(R.id.main_empty_view);
        mRvBlogs.setEmptyView(emptyView);

        //初始化HeaderView
        View header = mNavigationView.getHeaderView(0);
        RoundHeader roundHeader = header.findViewById(R.id.header_main_drawer_round_header);
        TextView tvWelcome = header.findViewById(R.id.header_main_drawer_tv_welcome);
        String username = (String) SpHelper.get(SpHelper.Key.KEY_USERNAME, "");
        roundHeader.setName(String.valueOf(username.charAt(0)));
        tvWelcome.setText(String.format(AppUtil.getString(R.string.drawer_normal_welcome), username));
    }

    private void initListener() {
        mTopBar.setOnImbClickListener(new TopBar.OnImbClickListener() {
            @Override
            public void onLeftImbClick() {

            }

            @Override
            public void onRightImbClick() {
                openLeftDrawer();
            }
        });

        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mine_my_blog:
                    PublishActivity.startAction(this);
                    break;
                case R.id.mine_favorites:
                    FavoritesActivity.startAction(this);
                    break;
                case R.id.mine_recycler_bin:
                    BinActivity.startAction(this);
                    break;
                case R.id.other_about_us:
                    break;
                case R.id.other_logout:
                    getPresenter().logout();
                    break;
                case R.id.other_exit_app:
                    finish();
                    break;
                default:
                    break;
            }
            return true;
        });

        mRefreshView.setOnRefreshListener(() -> getPresenter().refresh());

        mBlogAdapter.setOnClickListener(new BlogAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position, Blog blog) {
                if (!ButtonUtil.isFastClick(blog.id)) {
                    BlogActivity.startAction(MainActivity.this, blog);
                }
            }

            @Override
            public void onFavoriteClick(int position, Blog blog) {
                if (!ButtonUtil.isFastClick(blog.id)) {
                    getPresenter().favorite(!blog.isFavorite, blog.id, position);
                }
            }

            @Override
            public void onMoreClick(int position, Blog blog, View view) {

            }
        });

        mFabAdd.setOnClickListener(v -> EditActivity.startAction(this, EditActivity.Mode.PUBLISH, -1));
    }

    @Override
    public void replaceAll(List<Blog> blogs) {
        mBlogAdapter.replaceAll(blogs);
    }

    @Override
    public void insertEnd(List<Blog> blogs) {
        mBlogAdapter.insertEnd(blogs);
    }

    @Override
    public void favorite(boolean favor, int position) {
        mBlogAdapter.favorite(favor, position);
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

    @Override
    public void openLeftDrawer() {
        mDrawer.open();
    }

    @Override
    public void closeLeftDrawer() {
        mDrawer.close();
    }

    @Override
    public void toLogin() {
        LoginActivity.startAction(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isOpen()) {
            mDrawer.close();
        } else {
            super.onBackPressed();
        }
    }
}