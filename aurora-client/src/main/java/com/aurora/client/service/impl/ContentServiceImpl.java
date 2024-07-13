package com.aurora.client.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.aurora.client.common.dto.ChatDTO;
import com.aurora.client.common.entity.ContentDetailEntity;
import com.aurora.client.common.entity.ContentEntity;
import com.aurora.client.common.query.ContentQuery;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.mapper.ContentDetailMapper;
import com.aurora.client.mapper.ContentMapper;
import com.aurora.client.service.IContentService;
import com.aurora.client.utils.PeriodUtil;
import com.baidubce.qianfan.model.ApiErrorResponse;
import com.baidubce.qianfan.model.exception.ApiException;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    public ProfileVO getProfileByUserId(ContentQuery cq) {

        // 分页排序
        OrderItem oi = new OrderItem();
        oi.setColumn("content_create_time");
        oi.setAsc(false);
        Page<ContentEntity> page = cq.toMpPage(oi);

        // 分页条件以及查询
        Page<ContentEntity> p = lambdaQuery().eq(cq.getUserId() != null, ContentEntity::getUserId, cq.getUserId())
                .page(page);

        List<ContentEntity> records = p.getRecords();

        // 将分页数据封装为前端需要的形式
        ProfileVO pvo = encapsulateList(records);
        pvo.setUserId(cq.getUserId());

        return pvo;
    }

    /**
     * websocket处理对话问题
     */
    @Override
    public String handleChat(ChatDTO chat) {
        log.info("开始处理问答对话");
        JSONObject jo = new JSONObject();

        String answer = "";
        String detailId;
        String cId = chat.getContentId();
        String pdId = chat.getPreviousDetailId();

        if (StringUtils.isBlank(chat.getUserId())) {
            throw new ServiceException(NOT_ALLOW);
        }
        // contentId和previousDetailId要么都有，要么都没有
        if (
                (StringUtils.isBlank(cId) && StringUtils.isNotBlank(pdId)) || (StringUtils.isNotBlank(cId) && StringUtils.isBlank(pdId))
        ) {
            throw new ServiceException(NOT_ALLOW);
        }

        ContentEntity ce = new ContentEntity();
        ContentDetailEntity cde = new ContentDetailEntity();

        // 具体内容共享
        detailId = UUID.randomUUID().toString();
        cde.setDetailId(detailId);
        cde.setDetailAsk(chat.getAsk());
        cde.setDetailCreateTime(LocalDateTime.now());
        jo.put("detailId", pdId);

        if (StringUtils.isBlank(cId)) { // 新建对话
            // 内容
            cId = UUID.randomUUID().toString();
            ce.setContentId(cId);
            ce.setContentProfile(chat.getAsk());
            ce.setContentCreateTime(LocalDateTime.now());
            ce.setUserId(chat.getUserId());
            // 具体内容
            cde.setDetailParentId(detailId); // 父对话就是本身
            cde.setContentId(cId);
            contentMapper.insert(ce);
        } else { // 继续对话
            cde.setDetailParentId(pdId);
            cde.setContentId(cId);
        }
        // API调用
        try {
//            answer = QFUtil.ask(chat.getAsk());
            answer = "你好，我是默认回答" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
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
        jo.put("contentId", cId);
        jo.put("detailId", detailId);
        jo.put("answer", answer);
        return jo.toString();
    }

    /**
     * 封装概括区数据
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
