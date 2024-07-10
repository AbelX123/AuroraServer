package com.aurora.client.service.impl;

import com.aurora.client.common.dto.ChatDTO;
import com.aurora.client.common.entity.ContentDetailEntity;
import com.aurora.client.common.entity.ContentEntity;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.mapper.ContentDetailMapper;
import com.aurora.client.mapper.ContentMapper;
import com.aurora.client.service.IContentService;
import com.aurora.client.utils.PeriodUtil;
import com.aurora.client.utils.QFUtil;
import com.baidubce.qianfan.model.ApiErrorResponse;
import com.baidubce.qianfan.model.exception.ApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.aurora.client.common.enumeration.ResultCode.NOT_ALLOW;


@Slf4j
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements IContentService {

    @Autowired
    private ContentDetailMapper contentDetailMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Override
    public ContentVO getContentByContentId(String contentId) {
        return null;
    }

    @Override
    public ProfileVO getProfileByUserId(String userId) {
        QueryWrapper<ContentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .orderByDesc("content_create_time");
        List<ContentEntity> list = this.list(wrapper);
        ProfileVO vo = encapsulateList(list);
        vo.setUserId(userId);
        return vo;
    }

    /**
     * websocket处理对话问题
     */
    @Override
    public String handleChat(ChatDTO chat) {
        log.info("开始处理问答对话");
        String answer = "";
        if (null == chat.getUserId()) {
            throw new ServiceException(NOT_ALLOW);
        }
        if (null != chat.getContentId() && null == chat.getPreviousDetailId()) {
            throw new ServiceException(NOT_ALLOW);
        }

        ContentEntity ce = new ContentEntity();
        ContentDetailEntity cde = new ContentDetailEntity();

        // 具体内容共享
        cde.setDetailId(UUID.randomUUID().toString());
        cde.setDetailAsk(chat.getAsk());
        cde.setDetailCreateTime(LocalDateTime.now());

        if (null == chat.getContentId()) { // 新建对话
            // 内容
            String contentId = UUID.randomUUID().toString();
            ce.setContentId(contentId);
            ce.setContentProfile(chat.getAsk());
            ce.setContentCreateTime(LocalDateTime.now());
            ce.setUserId(chat.getUserId());
            // 具体内容
            cde.setDetailParentId(cde.getDetailId()); // 父对话就是本身
            cde.setContentId(contentId);
            contentMapper.insert(ce);
        } else { // 继续对话
            cde.setDetailParentId(chat.getPreviousDetailId());
            cde.setContentId(chat.getContentId());
        }
        // API调用
        try {
            answer = QFUtil.ask(chat.getAsk());
            cde.setDetailStatus("0000");
            cde.setDetailMsg("success");
            cde.setDetailAnswer(answer);
        } catch (ApiException e) {
            // 记录错误
            ApiErrorResponse eResp = e.getErrorResponse();
            log.error(eResp.toString());
            cde.setDetailStatus(eResp.getErrorCode().toString());
            cde.setDetailMsg(eResp.getErrorMsg());
            cde.setDetailAnswer(eResp.getErrorMsg());
        }
        contentDetailMapper.insert(cde);

        return answer;
    }

    /**
     * 封装概括区数据
     *
     * @param list
     * @return
     */
    private ProfileVO encapsulateList(List<ContentEntity> list) {
        ProfileVO pvo = new ProfileVO();

        // 使用Map来汇总相同时间的数据
        Map<String, List<ContentVO>> map = new HashMap<>();

        List<ProfileVO.AllProfile> allProfiles = new ArrayList<>();

        for (ContentEntity entity : list) {
            String key = PeriodUtil.timeToPeriod(entity.getContentCreateTime()).getDayName();
            ContentVO cvo = new ContentVO();
            cvo.setContentId(entity.getContentId());
            cvo.setContentProfile(entity.getContentProfile());
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(cvo);
        }
        map.forEach((key, value) -> {
            ProfileVO.AllProfile allProfile = new ProfileVO.AllProfile();
            allProfile.setTime(key);
            allProfile.setProfiles(value);
            allProfiles.add(allProfile);
        });
        pvo.setAllProfiles(allProfiles);
        return pvo;
    }

}
