package com.aurora.client.service.impl;

import com.aurora.client.common.entity.ContentDetailEntity;
import com.aurora.client.common.query.ContentQuery;
import com.aurora.client.common.vo.ContentDetailVO;
import com.aurora.client.mapper.ContentDetailMapper;
import com.aurora.client.service.IContentDetailService;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IContentDetailServiceImpl extends ServiceImpl<ContentDetailMapper, ContentDetailEntity> implements IContentDetailService {


    @Override
    public List<ContentDetailVO> getContentDetailByContentId(ContentQuery cq) {

        // 分页排序
        OrderItem oi = new OrderItem();
        oi.setColumn("detail_create_time");
        oi.setAsc(true);
        Page<ContentDetailEntity> page = cq.toMpPage(oi);

        // 分页条件以及查询
        Page<ContentDetailEntity> p = lambdaQuery().eq(cq.getContentId() != null, ContentDetailEntity::getContentId, cq.getContentId())
                .page(page);

        List<ContentDetailEntity> records = p.getRecords();
        List<ContentDetailVO> result = new ArrayList<>();
        records.forEach(record -> {
            ContentDetailVO vo = new ContentDetailVO();
            vo.setDetailId(record.getDetailId());
            vo.setAsk(record.getDetailAsk());
            vo.setAnswer(record.getDetailAnswer());
            result.add(vo);
        });

        return result;
    }
}
