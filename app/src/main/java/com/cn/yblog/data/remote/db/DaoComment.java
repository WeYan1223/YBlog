package com.cn.yblog.data.remote.db;

import com.cn.yblog.entity.Comment;
import com.cn.yblog.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2023/1/1
 * <p>version: 1.0
 * <p>update: none
 */
public class DaoComment {
    public static final String TABLE_Y_COMMENT = "y_comment";
    public static final String COLUMN_C_ID = "c_id";
    public static final String COLUMN_C_CONTENT = "c_content";
    public static final String COLUMN_C_U_ID = "c_u_id";
    public static final String COLUMN_C_B_ID = "c_b_id";
    public static final String COLUMN_C_TIME = "c_time";

    private final GaussHelper mGaussHelper;

    public DaoComment(GaussHelper gaussHelper) {
        mGaussHelper = gaussHelper;
    }

    /**
     * 插入数据
     *
     * @param comment 新评论
     * @return 影响行数
     */
    public int insert(Comment comment) {
        String sql = "INSERT INTO " + TABLE_Y_COMMENT + " ("
                + COLUMN_C_CONTENT + ", "
                + COLUMN_C_U_ID + ", "
                + COLUMN_C_B_ID + ", "
                + COLUMN_C_TIME + ") VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setString(1, comment.content);
            statement.setInt(2, comment.userId);
            statement.setInt(3, comment.blogId);
            statement.setLong(4, comment.time);
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
     * 获取博客ID为{@code blogId}的博客下的所有评论
     *
     * @param blogId 博客ID
     * @return 评论
     */
    public List<Comment> getComments(int blogId) {
        List<Comment> result = new ArrayList<>();
        String sql = "SELECT * FROM comment_detailed WHERE " + COLUMN_C_B_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, blogId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Comment comment = analysisComment(resultSet);
                result.add(comment);
            }
            return result;
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
        return null;
    }

    /**
     * 解析ResultSet为Comment对象
     *
     * @param resultSet ResultSet
     * @return Comment
     */
    private Comment analysisComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.id = resultSet.getInt(resultSet.findColumn(COLUMN_C_ID));
        comment.content = resultSet.getString(resultSet.findColumn(COLUMN_C_CONTENT));
        comment.userId = resultSet.getInt(resultSet.findColumn(COLUMN_C_U_ID));
        comment.userName = resultSet.getString(resultSet.findColumn(DaoUser.COLUMN_U_USERNAME));
        comment.blogId = resultSet.getInt(resultSet.findColumn(COLUMN_C_B_ID));
        comment.time = resultSet.getLong(resultSet.findColumn(COLUMN_C_TIME));
        return comment;
    }
}
