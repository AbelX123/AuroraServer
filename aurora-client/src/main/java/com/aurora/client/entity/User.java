package com.aurora.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("aurora_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

    private String userPassword;

    private String userPhone;
}
