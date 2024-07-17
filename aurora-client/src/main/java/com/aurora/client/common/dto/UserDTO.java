package com.aurora.client.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * UserDTO
 */
@Data
public class UserDTO {

    private String userId;

    @NotBlank(message = "用户名不能为空")
    @Size(max = 24, message = "用户名太长")
    private String username;

    @Size(max = 18, message = "密码最多18位")
    private String password;

    private String token;

    private String refreshToken;
}
