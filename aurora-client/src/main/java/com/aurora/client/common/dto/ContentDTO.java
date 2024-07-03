package com.aurora.client.common.dto;

import com.aurora.client.common.validation.ContentValidationGroup;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 前端传过来的content 内容
 */
@Data
@ToString
public class ContentDTO {

    @NotBlank(groups = ContentValidationGroup.Update.class, message = "内容编号不能为空")
    private String contentId;

    @NotBlank(message = "用户不能为空")
    private String userId;

    @NotBlank(message = "概括不能为空")
    private String contentProfile;

    @NotBlank(message = "内容不能为空")
    private String content;
}
