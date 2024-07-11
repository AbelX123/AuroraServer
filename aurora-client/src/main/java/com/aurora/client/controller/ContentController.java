package com.aurora.client.controller;

import com.aurora.client.common.CommonResult;
import com.aurora.client.common.query.ContentQuery;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.service.IContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private IContentService cs;

    /**
     * 依据内容编号获取内容
     */
    @GetMapping("/getContentDetailByContentId")
    public CommonResult<ContentVO> getContentDetailByContentId(@RequestParam(value = "contentId") ContentQuery contentDTO) {
        return CommonResult.success(cs.getContentDetailByContentId(contentDTO));
    }

    /**
     * 依据用户编号获取时间段的内容列表
     */
    @GetMapping("/getProfileByUserId")
    public CommonResult<ProfileVO> getProfileByUserId(ContentQuery cq) {
        log.info(cq.toString());
        CommonResult<ProfileVO> success = CommonResult.success(cs.getProfileByUserId(cq));
        log.info("success: {}", success.toString());
        return success;
    }
}
