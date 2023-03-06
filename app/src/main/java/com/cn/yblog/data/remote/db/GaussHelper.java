package com.cn.yblog.data.remote.db;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cn.yblog.service.RemoteService;
import com.cn.yblog.util.LogUtil;
import com.cn.yblog.util.ToastUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class GaussHelper {
    private static final String URL = "jdbc:opengauss://121.37.21.83:26000/blog_system";
    private static final String USER = "fengzzz";
    private static final String PASSWORD = "FengZzz666";
    private static GaussHelper INSTANCE;

    private Connection mConnection;
    private DaoUser mDaoUser;
    private DaoBlog mDaoBlog;
    private DaoFavorites mDaoFavorites;
    private DaoRecyclerBin mDaoRecyclerBin;
    private DaoComment mDaoComment;


    private GaussHelper() {

    }

    public static GaussHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GaussHelper();
        }
        return INSTANCE;
    }

    public void startDaemon() {
        RemoteService.getInstance().execute(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("TAG", "=============守护线程正在运行=============");
                    if (mConnection == null || mConnection.isClosed()) {
                        Class.forName("com.huawei.opengauss.jdbc.Driver");
                        mConnection = DriverManager.getConnection(URL, USER, PASSWORD);
                        Log.d("TAG", "=============数据库重新连接成功=============");
                        new Handler(Looper.getMainLooper()).post(() -> ToastUtil.show("数据库重新连接成功"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 连接数据库
     * <p>必须在启动页完成数据库连接
     */
    public void connect() throws ClassNotFoundException, SQLException {
        if (mConnection == null) {
            LogUtil.d("开始连接数据库: " + URL);
            Class.forName("com.huawei.opengauss.jdbc.Driver");
            mConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            LogUtil.d("连接数据库成功: " + URL);
        }
    }

    /**
     * 断开数据库连接
     */
    public void disconnect() {
        try {
            if (mConnection != null && !mConnection.isClosed()) {
                mConnection.close();
                mConnection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return mConnection;
    }

    /**
     * 获取{@link DaoUser}对象
     *
     * @return DaoUser
     */
    public DaoUser getDaoUser() {
        if (mDaoUser == null) {
            mDaoUser = new DaoUser(INSTANCE);
        }
        return mDaoUser;
    }

    /**
     * 获取{@link DaoBlog }对象
     *
     * @return DaoBlog
     */
    public DaoBlog getDaoBlog() {
        if (mDaoBlog == null) {
            mDaoBlog = new DaoBlog(INSTANCE);
        }
        return mDaoBlog;
    }

    /**
     * 获取{@link DaoRecyclerBin}对象
     *
     * @return DaoRecyclerBin
     */
    public DaoFavorites getDaoFavorites() {
        if (mDaoFavorites == null) {
            mDaoFavorites = new DaoFavorites(INSTANCE);
        }
        return mDaoFavorites;
    }

    /**
     * 获取{@link DaoRecyclerBin}对象
     *
     * @return DaoRecyclerBin
     */
    public DaoRecyclerBin getDaoRecyclerBin() {
        if (mDaoRecyclerBin == null) {
            mDaoRecyclerBin = new DaoRecyclerBin(INSTANCE);
        }
        return mDaoRecyclerBin;
    }

    /**
     * 获取{@link DaoComment}对象
     *
     * @return DaoComment
     */
    public DaoComment getDaoComment() {
        if (mDaoComment == null) {
            mDaoComment = new DaoComment(INSTANCE);
        }
        return mDaoComment;
    }
}
