package com.aurora.client.common.enumeration;

/**
 * 返回应答枚举
 */
public enum ResultCode {

    SUCCESS("0000", "success"),

    _404("404", "访问错误的资源"),

    _401("401", "身份未验证"),

    _403("403", "拒绝访问"),

    // 用户相关错误码从1000开始
    USER_EXIST("1000", "用户已存在"),
    USER_NOT_EXIST("1001", "用户不存在"),
    USER_PASSWORD_NOT_MATCH("1002", "用户名或密码错误"),

    // 共性异常提示从9000开始
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
