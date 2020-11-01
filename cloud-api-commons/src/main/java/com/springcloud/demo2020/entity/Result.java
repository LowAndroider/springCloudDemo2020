package com.springcloud.demo2020.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Djh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private int code;

    private String message;

    private T data;

    public Result(Integer code, String message) {
        this(code, message, null);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功！");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功！", data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(403, "请求失败");
    }

    public static <T> Result<T> commonResult(boolean flag) {
        if (flag) {
            return success();
        } else {
            return error();
        }
    }
}
