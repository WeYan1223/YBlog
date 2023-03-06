package com.cn.yblog.data.remote.db;

import com.cn.yblog.entity.Blog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DaoFavorites {
    public static final String TABLE_Y_FAVORITES = "y_favorites";
    public static final String COLUMN_F_ID = "f_id";
    public static final String COLUMN_F_U_ID = "f_u_id";
    public static final String COLUMN_F_B_ID = "f_b_id";
    public static final String COLUMN_F_TIME = "f_time";

    /**
     * 每次最多获取20条数据
     */
    private static final int LIMIT = 20;

    private final GaussHelper mGaussHelper;

    public DaoFavorites(GaussHelper gaussHelper) {
        mGaussHelper = gaussHelper;
    }

    /**
     * 插入数据
     *
     * @param userId 用户ID
     * @param blogId 博客Id
     * @param time   收藏时间
     * @return 返回影响行数，为1说明插入成功
     */
    public int insert(int userId, int blogId, long time) {
        String sql = "INSERT INTO " + TABLE_Y_FAVORITES + " ("
                + COLUMN_F_U_ID + ", "
                + COLUMN_F_B_ID + ", "
                + COLUMN_F_TIME + ") VALUES (?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, blogId);
            statement.setLong(3, time);
            return statement.executeUpdate();
        } catch (SQLException e) {
            GaussHelper.getInstance().disconnect();
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * 获取用户ID为{@code userId}所有收藏的博客
     *
     * @param userId 用户ID
     * @return 收藏的博客数据
     */
    public List<Blog> getFavorites(int userId) {
        List<Blog> result = new ArrayList<>();
        String sql = "SELECT * FROM favorites_detailed WHERE f_u_id = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Blog blog = analysisBlog(resultSet);
                blog.isFavorite = true;
                result.add(blog);
            }
        } catch (SQLException e) {
            GaussHelper.getInstance().disconnect();
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 删除数据（取消收藏）
     *
     * @param userId 用户ID
     * @param blogId 博客ID
     * @return 返回影响行数，为1说明删除成功
     */
    public int delete(int userId, int blogId) {
        String sql = "DELETE FROM " + TABLE_Y_FAVORITES + " WHERE " + COLUMN_F_U_ID + " = ? AND " + COLUMN_F_B_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, blogId);
            return statement.executeUpdate();
        } catch (SQLException e) {
            GaussHelper.getInstance().disconnect();
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 解析ResultSet为Blog对象
     *
     * @param resultSet ResultSet
     * @return Blog
     */
    private Blog analysisBlog(ResultSet resultSet) throws SQLException {
        Blog blog = new Blog();
        blog.id = resultSet.getInt(resultSet.findColumn(DaoBlog.COLUMN_B_ID));
        blog.authorId = resultSet.getInt(resultSet.findColumn(DaoBlog.COLUMN_B_AUTHOR_ID));
        blog.authorName = resultSet.getString(resultSet.findColumn(DaoUser.COLUMN_U_USERNAME));
        blog.title = resultSet.getString(resultSet.findColumn(DaoBlog.COLUMN_B_TITLE));
        blog.content = resultSet.getString(resultSet.findColumn(DaoBlog.COLUMN_B_CONTENT));
        blog.publishTime = resultSet.getLong(resultSet.findColumn(DaoBlog.COLUMN_B_PUBLISH_TIME));
        return blog;
    }
}
