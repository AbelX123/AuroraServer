package com.aurora.client.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("aurora_content_detail")
public class ContentDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("detail_id")
    private String detailId;
    private String detailAsk;
    private String detailAnswer;
    private String detailParentId;
    private LocalDateTime detailCreateTime;
    private String detailStatus;
    private String detailMsg;
    private String contentId;
}
