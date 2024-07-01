package com.aurora.client.controller;

import com.aurora.client.common.CommonResult;
import com.aurora.client.entity.Content;
import com.aurora.client.service.IContentService;
import com.aurora.client.vo.ContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentController {

    private IContentService contentService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * setter注入
     */
    @Autowired
    public void setContentService(IContentService contentService) {
        this.contentService = contentService;
    }

    /**
     *  问答入库
     */
    @GetMapping("/saveContent")
    public CommonResult<Content> saveContent(Content content) {
        redisTemplate.opsForValue().set("aaa", "aaa");
        return CommonResult.success(content);
    }

    @GetMapping("/getContentByUserId")
    public CommonResult<ContentVO> getContentByContentId(String contentId) {
        return contentService.getContentByContentId(contentId);
    }

}
