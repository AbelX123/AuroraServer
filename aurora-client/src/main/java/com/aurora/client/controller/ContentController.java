package com.aurora.client.controller;

import com.aurora.client.common.CommonResult;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.service.IContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private IContentService cs;

    /**
     * 依据内容编号获取内容
     */
    @GetMapping("/getContentByContentId")
    public CommonResult<ContentVO> getContentByContentId(@RequestParam(value = "contentId") String contentId) {
        return CommonResult.success(cs.getContentByContentId(contentId));
    }

    /**
     * 依据用户编号获取时间段的内容列表
     */
    @GetMapping("/getProfileByUserId")
    public CommonResult<ProfileVO> getProfileByUserId(HttpServletRequest request, @RequestParam(value = "userId") String userId) {
        return CommonResult.success(cs.getProfileByUserId(userId));
    }
}
