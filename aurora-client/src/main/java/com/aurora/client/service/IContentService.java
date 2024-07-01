package com.aurora.client.service;

import com.aurora.client.common.CommonResult;
import com.aurora.client.entity.Content;
import com.aurora.client.vo.ContentVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IContentService extends IService<Content> {

    CommonResult<ContentVO> getContentByContentId(String contentId);
}
