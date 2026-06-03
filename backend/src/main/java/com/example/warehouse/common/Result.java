package com.example.warehouse.common;
import lombok.Data;
/**
 * 所有接口返回都用这个格式，code=200成功，其他都是失败
 */
@Data
public class Result {
    private Integer code;      // 状态码：200成功，500失败
    private String message;    // 提示信息
    private Object data;       // 返回数据

    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static Result error(Integer code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
