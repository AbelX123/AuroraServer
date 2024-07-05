package com.aurora.client.service.impl;

import com.aurora.client.common.dto.ContentDTO;
import com.aurora.client.common.entity.ContentEntity;
import com.aurora.client.common.po.ContentPO;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.mapper.ContentMapper;
import com.aurora.client.service.IContentService;
import com.aurora.client.utils.PeriodUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.aurora.client.common.enumeration.ResultCode.MIDDLEWARE_ERROR;


@Slf4j
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements IContentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ContentVO getContentByContentId(String contentId) {
        ContentVO vo = new ContentVO();
        Query query = new Query();
        query.addCriteria(Criteria.where("content_id").is(contentId));
        ContentPO po = mongoTemplate.findOne(query, ContentPO.class);
        if (null != po && null != po.getContent()) {
            vo.setContent(po.getContent());
        }
        return vo;
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

    @Override
    public boolean saveContent(ContentDTO contentDTO) {
        boolean save;
        ContentPO contentPO = new ContentPO();
        String contentId = UUID.randomUUID().toString();

        ContentEntity contentEntity = new ContentEntity();
        BeanUtils.copyProperties(contentDTO, contentEntity);
        contentEntity.setContentId(contentId);
        contentEntity.setContentCreateTime(LocalDateTime.now());
        try {
            save = this.save(contentEntity);
        } catch (Exception e) {
            log.error("MYSQL存储失败:{}", e.getMessage());
            throw new ServiceException(MIDDLEWARE_ERROR);
        }

        contentPO.setContentId(contentId);
        contentPO.setContent(contentDTO.getContent());
        try {
            mongoTemplate.insert(contentPO);
        } catch (Exception e) {
            log.error("MONGODB存储失败:[{}]", contentDTO);
            throw new ServiceException(MIDDLEWARE_ERROR);
        }

        return save;
    }

    @Override
    public boolean updateContent(ContentDTO contentDTO) {
        String contentId = contentDTO.getContentId();
        Query query = new Query();
        query.addCriteria(Criteria.where("content_id").is(contentId));
        Update u = new Update();
        u.set("content", contentDTO.getContent());
        try {
            mongoTemplate.updateFirst(query, u, ContentPO.class, "content");
            return true;
        } catch (Exception e) {
            log.error("[{}]更新文档错误:{}", contentId, e.getMessage());
            throw new ServiceException(MIDDLEWARE_ERROR);
        }
    }
}
