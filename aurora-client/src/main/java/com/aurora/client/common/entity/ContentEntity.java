package com.aurora.client.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("aurora_content")
@ToString
public class ContentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内容编号
     */
    @TableId("content_id")
    private String contentId;

    /**
     * 内容概括
     */
    private String contentProfile;

    /**
     * 内容创建时间
     */
    private LocalDateTime contentCreateTime;

    /**
     * 内容创建人
     */
    private String userId;
}
