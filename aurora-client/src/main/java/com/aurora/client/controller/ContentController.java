package com.aurora.client.controller;

import com.aurora.client.common.CommonResult;
import com.aurora.client.common.dto.ContentDTO;
import com.aurora.client.common.validation.ContentValidationGroup;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.service.IContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.aurora.client.common.enumeration.ResultCode.OTHER_ERROR;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private IContentService cs;

    /**
     * 问答入库
     */
    @PostMapping("/saveContent")
    public CommonResult<ContentVO> saveContent(@RequestBody @Validated ContentDTO contentDTO) {
        boolean save = cs.saveContent(contentDTO);
        if (save) {
            return CommonResult.success();
        } else {
            return CommonResult.failure(OTHER_ERROR);
        }
    }

    /**
     * 问答更新
     */
    @PutMapping("/updateContent")
    public CommonResult<ContentVO> updateContent(@RequestBody
                                                 @Validated(value = ContentValidationGroup.Update.class) ContentDTO contentDTO) {
        boolean update = cs.updateContent(contentDTO);
        if (update) {
            return CommonResult.success();
        } else {
            return CommonResult.failure(OTHER_ERROR);
        }
    }

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
    public CommonResult<ProfileVO> getProfileByUserId(@RequestParam(value = "userId") String userId) {
        return CommonResult.success(cs.getProfileByUserId(userId));
    }
}
