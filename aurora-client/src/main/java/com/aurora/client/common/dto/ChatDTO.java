package com.aurora.client.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 前端传给后端的对话DTO
 */
@Data
public class ChatDTO {

    @NotBlank(message = "用户编号不能为空")
    private String userId;

    private String contentId;

    @NotBlank(message = "问题不能为空")
    @Size(max = 1024, message = "问题太长了，请简短描述")
    private String ask;

    private String previousDetailId;
}
