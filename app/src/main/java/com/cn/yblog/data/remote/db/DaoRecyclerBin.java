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
public class DaoRecyclerBin {
    public static final String TABLE_Y_RECYCLER_BIN = "y_recycler_bin";
    public static final String COLUMN_R_ID = "r_id";
    public static final String COLUMN_R_AUTHOR_ID = "r_author_id";
    public static final String COLUMN_R_TITLE = "r_title";
    public static final String COLUMN_R_CONTENT = "r_content";
    public static final String COLUMN_R_PUBLISH_TIME = "r_publish_time";

    /**
     * 每次最多获取20条数据
     */
    private static final int LIMIT = 20;

    private final GaussHelper mGaussHelper;

    public DaoRecyclerBin(GaussHelper gaussHelper) {
        mGaussHelper = gaussHelper;
    }

    /**
     * 获取{@link DaoUser#COLUMN_U_ID}为{@code authorId}的用户的回收站所有博客数据
     *
     * @param authorId 作者ID
     * @return 回收站的博客数据
     */
    public List<Blog> getBlogs(int authorId) {
        List<Blog> result = new ArrayList<>();
        String sql = "SELECT * FROM bin_detailed WHERE " + COLUMN_R_AUTHOR_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, authorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Blog blog = analysisBlog(resultSet);
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
     * 删除数据
     *
     * @param id ID
     * @return 返回影响行数，为1说明删除成功
     */
    public int delete(int id) {
        String sql = "DELETE FROM " + TABLE_Y_RECYCLER_BIN + " WHERE " + COLUMN_R_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
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
     * 清空回收站
     *
     * @param authorId 作者ID
     * @return 返回影响行数
     */
    public int clear(int authorId) {
        String sql = "DELETE FROM " + TABLE_Y_RECYCLER_BIN + " WHERE " + COLUMN_R_AUTHOR_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, authorId);
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
        blog.id = resultSet.getInt(resultSet.findColumn(COLUMN_R_ID));
        blog.authorId = resultSet.getInt(resultSet.findColumn(COLUMN_R_AUTHOR_ID));
        blog.authorName = resultSet.getString(resultSet.findColumn(DaoUser.COLUMN_U_USERNAME));
        blog.title = resultSet.getString(resultSet.findColumn(COLUMN_R_TITLE));
        blog.content = resultSet.getString(resultSet.findColumn(COLUMN_R_CONTENT));
        blog.publishTime = resultSet.getLong(resultSet.findColumn(COLUMN_R_PUBLISH_TIME));
        return blog;
    }
}