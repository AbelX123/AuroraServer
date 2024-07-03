package com.aurora.client.common.enumeration;

/**
 * 返回应答枚举
 */
public enum ResultCode {

    SUCCESS("0000", "success"),

    VALIDATE_ERROR("9000", "参数验证错误"),

    MIDDLEWARE_ERROR("9001", "第三方异常，请稍后重试"),

    OTHER_ERROR("9999", "系统异常，请稍后重试");

    private final String status;
    private final String message;

    ResultCode(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}