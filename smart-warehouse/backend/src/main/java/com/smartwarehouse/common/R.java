package com.smartwarehouse.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer code;
    private String message;
    private T data;
    private Long total;
    
    public R() {}
    
    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> R<T> ok() {
        return new R<>(200, "操作成功", null);
    }
    
    public static <T> R<T> ok(T data) {
        return new R<>(200, "操作成功", data);
    }
    
    public static <T> R<T> ok(String message, T data) {
        return new R<>(200, message, data);
    }
    
    public static <T> R<T> fail(String message) {
        return new R<>(500, message, null);
    }
    
    public static <T> R<T> fail(Integer code, String message) {
        return new R<>(code, message, null);
    }
    
    public R<T> total(Long total) {
        this.total = total;
        return this;
    }
}