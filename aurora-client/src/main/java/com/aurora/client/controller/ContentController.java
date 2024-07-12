package com.aurora.client.controller;

import com.aurora.client.common.CommonResult;
import com.aurora.client.common.enumeration.ResultCode;
import com.aurora.client.common.query.ContentQuery;
import com.aurora.client.common.vo.ContentDetailVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.service.IContentDetailService;
import com.aurora.client.service.IContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private IContentService cs;

    @Autowired
    private IContentDetailService cds;

    /**
     * 依据用户编号获取时间段的内容列表
     */
    @GetMapping("/getProfileByUserId")
    public CommonResult<ProfileVO> getProfileByUserId(ContentQuery cq) {
        return CommonResult.success(cs.getProfileByUserId(cq));
    }

    /**
     * 依据内容编号获取内容
     */
    @GetMapping("/getContentDetailByContentId")
    public CommonResult<List<ContentDetailVO>> getContentDetailByContentId(ContentQuery cq) {
        if (cq.getUserId() == null) {
            throw new ServiceException(ResultCode.NOT_ALLOW);
        }
        List<ContentDetailVO> result = cds.getContentDetailByContentId(cq);
        return CommonResult.success(result);
    }
}
