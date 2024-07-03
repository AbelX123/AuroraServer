package com.aurora.client.service;

import com.aurora.client.common.dto.ContentDTO;
import com.aurora.client.common.entity.ContentEntity;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 问答内容相关
 */
public interface IContentService extends IService<ContentEntity> {

    boolean saveContent(ContentDTO contentDTO);

    boolean updateContent(ContentDTO contentDTO);

    ContentVO getContentByContentId(String contentId);

    ProfileVO getProfileByUserId(String userId);
}
