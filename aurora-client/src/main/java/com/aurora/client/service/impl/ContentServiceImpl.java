package com.aurora.client.service.impl;

import com.aurora.client.common.CommonResult;
import com.aurora.client.entity.Content;
import com.aurora.client.mapper.ContentMapper;
import com.aurora.client.service.IContentService;
import com.aurora.client.vo.ContentVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements IContentService {

    @Override
    public CommonResult<ContentVO> getContentByContentId(String contentId) {
        Content content = this.getById(contentId);
        return null;
    }
}
