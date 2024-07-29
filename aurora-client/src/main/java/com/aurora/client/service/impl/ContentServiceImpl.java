package com.aurora.client.service.impl;

import com.aurora.client.common.dto.ChatDTO;
import com.aurora.client.common.entity.ContentEntity;
import com.aurora.client.common.query.ContentQuery;
import com.aurora.client.common.vo.ContentVO;
import com.aurora.client.common.vo.ProfileVO;
import com.aurora.client.mapper.ContentMapper;
import com.aurora.client.service.IContentService;
import com.aurora.client.utils.PeriodUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements IContentService {

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

    @Override
    public void saveContent(String contentId, ChatDTO chatDTO) {
        ContentEntity entity = new ContentEntity();
        entity.setContentProfile(chatDTO.getAsk());
        entity.setUserId(chatDTO.getUserId());
        entity.setContentId(contentId);
        entity.setContentCreateTime(LocalDateTime.now());
        this.save(entity);
    }

    /**
     * 封装概括区数据
     */
    private ProfileVO encapsulateList(List<ContentEntity> list) {
        ProfileVO pvo = new ProfileVO();

        // 自定义比较器
        Comparator<String> comparator = Comparator.comparingInt(Integer::parseInt);

        // 使用Map来汇总相同时间的数据
        Map<String, List<ContentVO>> map = new TreeMap<>(comparator);

        List<ProfileVO.AllProfile> allProfiles = new ArrayList<>();

        for (ContentEntity entity : list) {
            int keyI = PeriodUtil.timeToPeriod(entity.getContentCreateTime()).getDayBefore();
            String key = String.valueOf(keyI);
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
            // 将key转化为Period的value
            allProfile.setTime(PeriodUtil.getDayNameFromDayBefore(Integer.parseInt(key)));
            allProfile.setProfiles(value);
            allProfiles.add(allProfile);
        });

        pvo.setAllProfiles(allProfiles);
        return pvo;
    }
}
