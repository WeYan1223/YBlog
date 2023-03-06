package com.cn.yblog.entity;

/**
 * description: 博客实体类
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/25
 * <p>version: 1.0
 * <p>update: none
 */
public class Blog {
    /**
     * 博客ID
     */
    public int id;

    /**
     * 作者ID
     */
    public int authorId;

    /**
     * 作者用户名
     */
    public String authorName;

    /**
     * 博客标题
     */
    public String title;

    /**
     * 博客内容
     */
    public String content;

    /**
     * 博客发布时间
     */
    public long publishTime;

    /**
     * 是否被当前用户收藏
     */
    public boolean isFavorite;

    public Blog() {
    }

    public Blog(int id, int authorId, String authorName, String title, String content, long publishTime, boolean isFavorite) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.title = title;
        this.content = content;
        this.publishTime = publishTime;
        this.isFavorite = isFavorite;
    }
}
