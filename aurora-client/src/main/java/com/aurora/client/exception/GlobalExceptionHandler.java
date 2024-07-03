package com.aurora.client.exception;

import com.aurora.client.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.aurora.client.common.enumeration.ResultCode.OTHER_ERROR;
import static com.aurora.client.common.enumeration.ResultCode.VALIDATE_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult<Object> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        log.error("参数校验失败:{}", errors);
        return CommonResult.failure(VALIDATE_ERROR, errors);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<Object> serviceExceptionHandler(ServiceException e) {
        return CommonResult.failure(e.getResultCode());
    }

    /**
     * 其他未捕获异常
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult<Object> exception(Exception e) {
        return CommonResult.failure(OTHER_ERROR);
    }

}
