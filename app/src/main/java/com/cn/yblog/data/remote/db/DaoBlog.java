package com.cn.yblog.data.remote.db;

import com.cn.yblog.entity.Blog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class DaoBlog {
    public static final String TABLE_Y_BLOG = "y_blog";
    public static final String COLUMN_B_ID = "b_id";
    public static final String COLUMN_B_AUTHOR_ID = "b_author_id";
    public static final String COLUMN_B_TITLE = "b_title";
    public static final String COLUMN_B_CONTENT = "b_content";
    public static final String COLUMN_B_PUBLISH_TIME = "b_publish_time";

    /**
     * 每次最多获取20条数据
     */
    private static final int LIMIT = 20;

    private final GaussHelper mGaussHelper;

    public DaoBlog(GaussHelper gaussHelper) {
        mGaussHelper = gaussHelper;
    }

    /**
     * 插入数据
     *
     * @param blog Blog
     * @return 返回影响行数，为1说明插入成功
     */
    public int insert(Blog blog) {
        String sql = "INSERT INTO " + TABLE_Y_BLOG + " ("
                + COLUMN_B_AUTHOR_ID + ", "
                + COLUMN_B_TITLE + ", "
                + COLUMN_B_CONTENT + ", "
                + COLUMN_B_PUBLISH_TIME + ") VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, blog.authorId);
            statement.setString(2, blog.title);
            statement.setString(3, blog.content);
            statement.setLong(4, blog.publishTime);
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
     * 恢复数据
     *
     * @param blog Blog
     * @return 返回影响行数，为1说明插入成功
     */
    public int recovery(Blog blog) {
        String sql = "INSERT INTO " + TABLE_Y_BLOG + " ("
                + COLUMN_B_ID + ", "
                + COLUMN_B_AUTHOR_ID + ", "
                + COLUMN_B_TITLE + ", "
                + COLUMN_B_CONTENT + ", "
                + COLUMN_B_PUBLISH_TIME + ") VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, blog.id);
            statement.setInt(2, blog.authorId);
            statement.setString(3, blog.title);
            statement.setString(4, blog.content);
            statement.setLong(5, blog.publishTime);
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
     * 获取{@link DaoBlog#COLUMN_B_ID}为{@code blogId}的博客
     *
     * @param blogId 博客ID
     * @return 博客
     */
    public Blog getBlog(int blogId) {
        String sql = "SELECT * FROM blog_detailed WHERE " + COLUMN_B_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, blogId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                return analysisBlog(resultSet);
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
     * 获取u_id为{@code userId}的用户的所有博客
     *
     * @param userId 用户ID
     * @return 博客
     */
    public List<Blog> getBlogs(int userId) {
        List<Blog> result = new ArrayList<>();
        String sql = "SELECT * FROM blog_detailed WHERE " + COLUMN_B_AUTHOR_ID + " = ?";
        PreparedStatement statement = null;
        try {
            Set<Integer> favBlogIdSet = getFavBlogIdSet(userId);
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                Blog blog = analysisBlog(resultSet);
                blog.isFavorite = favBlogIdSet.contains(blog.id);
                result.add(blog);
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
        return result;
    }

    /**
     * 获取所有用户的博客数据，先获取全部，后面再做加载更多(每次最多获取{@link DaoBlog#LIMIT}条)
     *
     * @param userId 用户ID
     * @return 博客数据
     */
    public List<Blog> getBlogs(int userId, int offset) {
        List<Blog> result = new ArrayList<>();
        String sql = "SELECT * FROM blog_detailed";
        PreparedStatement statement = null;
        try {
            Set<Integer> favBlogIdSet = getFavBlogIdSet(userId);
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet != null && resultSet.next()) {
                Blog blog = analysisBlog(resultSet);
                blog.isFavorite = favBlogIdSet.contains(blog.id);
                result.add(blog);
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
        return result;
    }

    /**
     * 更新数据
     *
     * @param blog Blog
     * @return 返回影响行数，为1说明更新成功
     */
    public int update(Blog blog) {
        String sql = "UPDATE " + TABLE_Y_BLOG + " SET "
                + COLUMN_B_TITLE + " = ?, "
                + COLUMN_B_CONTENT + " = ?, "
                + COLUMN_B_PUBLISH_TIME + " = ? "
                + "WHERE " + COLUMN_B_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setString(1, blog.title);
            statement.setString(2, blog.content);
            statement.setLong(3, blog.publishTime);
            statement.setInt(4, blog.id);
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
     * 删除数据
     *
     * @param blogId 博客ID
     * @return 返回影响行数，为1说明删除成功
     */
    public int delete(int blogId) {
        String sql = "DELETE FROM " + TABLE_Y_BLOG + " WHERE " + COLUMN_B_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, blogId);
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
     * 获取{@code userId}收藏的所有博客ID
     *
     * @param userId 用户ID
     * @return Set
     */
    private Set<Integer> getFavBlogIdSet(int userId) {
        Set<Integer> result = new HashSet<>();
        String sql = "SELECT * FROM " + DaoFavorites.TABLE_Y_FAVORITES + " WHERE " + DaoFavorites.COLUMN_F_U_ID + " = ?";
        PreparedStatement statement = null;
        try {
            statement = mGaussHelper.getConnection().prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int blogId = resultSet.getInt(resultSet.findColumn(DaoFavorites.COLUMN_F_B_ID));
                result.add(blogId);
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
     * 解析ResultSet为Blog对象
     *
     * @param resultSet ResultSet
     * @return Blog
     */
    private Blog analysisBlog(ResultSet resultSet) throws SQLException {
        Blog blog = new Blog();
        blog.id = resultSet.getInt(resultSet.findColumn(COLUMN_B_ID));
        blog.authorId = resultSet.getInt(resultSet.findColumn(COLUMN_B_AUTHOR_ID));
        blog.authorName = resultSet.getString(resultSet.findColumn(DaoUser.COLUMN_U_USERNAME));
        blog.title = resultSet.getString(resultSet.findColumn(COLUMN_B_TITLE));
        blog.content = resultSet.getString(resultSet.findColumn(COLUMN_B_CONTENT));
        blog.publishTime = resultSet.getLong(resultSet.findColumn(COLUMN_B_PUBLISH_TIME));
        return blog;
    }
}