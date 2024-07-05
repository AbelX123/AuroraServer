package com.aurora.client.common;

import com.aurora.client.common.enumeration.ResultCode;
import lombok.Data;

import java.io.Serializable;

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

    private CommonResult(Builder<T> builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.data = builder.data;
        this.sign = builder.sign;
    }

    public static <T> Builder<T> newBuilder() {
        return new Builder<>();
    }

    /**
     * 成功的应答
     */
    public static <T> CommonResult<T> success() {
        return CommonResult.<T>newBuilder().status(ResultCode.SUCCESS.getStatus())
                .message(ResultCode.SUCCESS.getMessage())
                .build();
    }

    /**
     * 成功含有数据的应答
     */
    public static <T> CommonResult<T> success(T data) {
        return CommonResult.<T>newBuilder().status(ResultCode.SUCCESS.getStatus())
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .sign(null)
                .build();
    }

    /**
     * 失败的应答
     */
    public static <T> CommonResult<T> failure(ResultCode failure) {
        // 静态范型方法调用
        return CommonResult.<T>newBuilder().status(failure.getStatus())
                .message(failure.getMessage())
                .build();
    }

    /**
     * 失败含有数据的应答
     */
    public static <T> CommonResult<T> failure(ResultCode failure, T data) {
        // 静态范型方法调用
        return CommonResult.<T>newBuilder().status(failure.getStatus())
                .message(failure.getMessage())
                .data(data)
                .build();
    }


    // 构造类
    @Data
    public static class Builder<T> {
        private String status;
        private String message;
        private T data;
        private String sign;

        private Builder() {
        }

        public Builder<T> status(String status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> sign(String sign) {
            this.sign = sign;
            return this;
        }

        public CommonResult<T> build() {
            return new CommonResult<>(this);
        }
    }

}
