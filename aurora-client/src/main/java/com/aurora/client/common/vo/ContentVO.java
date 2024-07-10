package com.aurora.client.common.vo;

import com.aurora.client.common.entity.ContentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 内容VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContentVO extends ContentEntity {
    private String contentId;
}
