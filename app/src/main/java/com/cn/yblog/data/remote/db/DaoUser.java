package com.cn.yblog.data.remote.db;

import com.cn.yblog.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/23
 * <p>version: 1.0
 * <p>update: none
 */
public class DaoUser {
    public static final String TABLE_Y_USER = "y_user";
    public static final String COLUMN_U_ID = "u_id";
    public static final String COLUMN_U_USERNAME = "u_username";
    public static final String COLUMN_U_PASSWORD = "u_password";

    private final GaussHelper mGaussHelper;

    public DaoUser(GaussHelper gaussHelper) {
        mGaussHelper = gaussHelper;
    }

    /**
     * 插入数据
     *
     * @param user User
     * @return 返回影响行数，为1说明插入成功
     */
    public int insert(User user) {
        String sql = "INSERT INTO " + TABLE_Y_USER + " ("
                + COLUMN_U_USERNAME + ", "
                + COLUMN_U_PASSWORD + ") VALUES (?, ?)";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setString(1, user.username);
            statement.setString(2, user.password);
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
     * 通过{@code id}获取{@link User}对象
     *
     * @param id 用户id
     * @return User
     */
    public User get(int id) {
        String sql = "SELECT * FROM " + TABLE_Y_USER + " WHERE " + COLUMN_U_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return analysisUser(resultSet);
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
        return null;
    }

    /**
     * 通过用户名获取{@link User}对象
     *
     * @param username 用户名
     * @return User
     */
    public User get(String username) {
        PreparedStatement statement = null;
        String sql = "SELECT * FROM " + TABLE_Y_USER + " WHERE " + COLUMN_U_USERNAME + " = ?";
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return analysisUser(resultSet);
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
        return null;
    }

    /**
     * 通过用户名和密码获取{@link User}对象
     *
     * @param username 用户名
     * @param password 密码
     * @return User
     */
    public User get(String username, String password) {
        String sql = "SELECT * FROM " + TABLE_Y_USER + " WHERE " + COLUMN_U_USERNAME + " = ? AND " + COLUMN_U_PASSWORD + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return analysisUser(resultSet);
            }
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
        return null;
    }

    /**
     * 更新{@link User}信息
     *
     * @param user User
     * @return 返回影响行数
     */
    public int update(User user) {
        String sql = "UPDATE " + TABLE_Y_USER + " SET "
                + COLUMN_U_USERNAME + " = ?, "
                + COLUMN_U_PASSWORD + " = ? WHERE "
                + COLUMN_U_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setString(1, user.username);
            statement.setString(2, user.password);
            statement.setInt(3, user.id);
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
     * 删除{@link User}信息
     *
     * @param userId 用户ID
     * @return 返回影响行数
     */
    public int delete(int userId) {
        String sql = "DELETE FROM " + TABLE_Y_USER + " WHERE " + COLUMN_U_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
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
     * 解析ResultSet为User对象
     *
     * @param resultSet ResultSet
     * @return User
     */
    private User analysisUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.id = resultSet.getInt(resultSet.findColumn(COLUMN_U_ID));
        user.username = resultSet.getString(resultSet.findColumn(COLUMN_U_USERNAME));
        user.password = resultSet.getString(resultSet.findColumn(COLUMN_U_PASSWORD));
        return user;
    }
}
