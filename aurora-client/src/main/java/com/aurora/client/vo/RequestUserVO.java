package com.aurora.client.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Data
public class RequestUserVO {

    @NotNull(message = "用户名不能为空")
    private String username;

    private String email;

    @NotNull(message = "验证码不能为空")
    private String verifyCode;
}
