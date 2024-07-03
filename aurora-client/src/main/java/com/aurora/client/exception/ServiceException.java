package com.aurora.client.exception;

import com.aurora.client.common.enumeration.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)    // 是否调用父类的equals和hashcode方法
@Data
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private ResultCode resultCode;

    public ServiceException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
