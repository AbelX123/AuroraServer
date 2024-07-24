package com.aurora.client.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@TableName("aurora_config")
public class ConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String configKey;
    private String configValue;
}
