package com.aurora.client.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.aurora.client.common.enumeration.ResultCode;
import lombok.Data;

import java.io.Serializable;

import static com.aurora.client.common.enumeration.ResultCode.SUCCESS;

/**
 * 全局应答
 */
@Data
public class CommonResult<T> implements Serializable {

    // 应答码
    private String status;
    // 应答描述
    private String message;
    // 应答信息
    private T data;
    // 重要信息签名
    private String sign;

    public CommonResult(ResultCode code) {
        this.status = code.getStatus();
        this.message = code.getMessage();
    }

    public CommonResult(ResultCode code, T data) {
        this.status = code.getStatus();
        this.message = code.getMessage();
        this.data = data;
    }

    public CommonResult(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public CommonResult(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功，没有data
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(SUCCESS);
    }

    /**
     * 返回成功，有data
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(SUCCESS, data);
    }

    /**
     * 返回失败，没有data
     */
    public static <T> CommonResult<T> failure(ResultCode code) {
        return new CommonResult<>(code);
    }

    /**
     * 返回失败，有data
     */
    public static <T> CommonResult<T> failure(ResultCode code, T data) {
        return new CommonResult<>(code, data);
    }

    /**
     * 转换为json字符串
     */
    public String toJsonString() {
        return JSON.toJSONString(this, JSONWriter.Feature.IgnoreNonFieldGetter);
    }

}
