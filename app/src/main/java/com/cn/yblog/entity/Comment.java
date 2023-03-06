package com.cn.yblog.entity;

/**
 * description: 评论实体类
 *
 * @author yan.w.s@qq.com
 * <p>time: 2023/1/1
 * <p>version: 1.0
 * <p>update: none
 */
public class Comment {
    /**
     * 评论ID
     */
    public int id;

    /**
     * 评论内容
     */
    public String content;

    /**
     * 发起评论的用户ID
     */
    public int userId;

    /**
     * 发起评论的用户名
     */
    public String userName;

    /**
     * 评论的博客ID
     */
    public int blogId;

    /**
     * 评论时间
     */
    public long time;

    public Comment() {

    }

    public Comment(int id, String content, int userId, String userName, int blogId, long time) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.blogId = blogId;
        this.time = time;
    }
}
