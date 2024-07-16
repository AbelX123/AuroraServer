package com.aurora.client.common.vo;

import lombok.Data;

/**
 * UserVO
 */
@Data
public class UserVO {

    private String userId;

    private String username;

    private String token;

    private String refresh_token;
}
