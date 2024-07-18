package com.aurora.client.common.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * UserVO
 */
@Data
@ToString
public class UserVO implements Serializable {

    private String userId;

    private String username;

    private String token;

    private String refreshToken;
}
