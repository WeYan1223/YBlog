package com.cn.yblog.entity;

/**
 * description: 用户实体类
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/23
 * <p>version: 1.0
 * <p>update: none
 */
public class User {
    /**
     * 用户ID
     */
    public int id;

    /**
     * 用户名
     */
    public String username;

    /**
     * 密码
     */
    public String password;

    public User() {

    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
