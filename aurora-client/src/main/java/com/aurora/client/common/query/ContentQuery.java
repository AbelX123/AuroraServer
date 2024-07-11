package com.aurora.client.common.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 前端传过来的content 内容
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ContentQuery extends PageQuery implements Serializable {

    private String contentId;

    @NotBlank(message = "用户不能为空")
    private String userId;
}
