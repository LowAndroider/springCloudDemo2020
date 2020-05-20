package com.springcloud.demo2020.entity;

import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Djh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private int code;

    private String message;

    private T data;

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }

    public static <T> CommonResult<T> success() {
        return new CommonResult<>(200, "操作成功！");
    }

    public static <T> CommonResult<T> error() {
        return new CommonResult<>(403, "请求失败");
    }

    public static <T> CommonResult<T> commonResult(boolean flag) {
        if (flag) {
            return success();
        } else {
            return error();
        }
    }
}
