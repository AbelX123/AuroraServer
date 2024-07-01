package com.aurora.client.common.enumeration;

/**
 * 返回应答枚举
 */
public enum ResultEnum {

    SUCCESS("0000", "success");

    private final String status;
    private final String message;

    ResultEnum(String status, String message) {
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
