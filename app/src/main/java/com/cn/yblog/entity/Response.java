package com.cn.yblog.entity;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/22
 * <p>version: 1.0
 * <p>update: none
 */
public class Response<T> {
    @IntDef({Code.CODE_SUCCESS, Code.CODE_FAILURE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Code {
        int CODE_SUCCESS = 0x0001;
        int CODE_FAILURE = 0x0002;
    }

    private @Code
    int code;
    private T data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(@Code int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
