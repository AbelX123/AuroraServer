package com.aurora.client.service;

import com.aurora.client.common.dto.ChatDTO;
import com.aurora.client.common.entity.ContentDetailEntity;
import com.aurora.client.common.query.ContentQuery;
import com.aurora.client.common.vo.ContentDetailVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IContentDetailService extends IService<ContentDetailEntity> {
    List<ContentDetailVO> getContentDetailByContentId(ContentQuery contentDTO);

    void saveContentDetail(String detailId, String contentId, String detailStatus, String detailMessage, String allAnswer, ChatDTO chatDTO);
}
