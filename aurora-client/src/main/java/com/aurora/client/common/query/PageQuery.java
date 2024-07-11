package com.aurora.client.common.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PageQuery {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String sortBy;
    private Boolean isAsc = true;

    public <T> Page<T> toMpPage(OrderItem... items) {
        // 1.分页条件
        Page<T> page = Page.of(pageNo, pageSize);
        // 2.排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            // 不为空
            if (isAsc) {
                page.addOrder(OrderItem.ascs(sortBy));
            } else {
                page.addOrder(OrderItem.descs(sortBy));
            }
        } else if (items != null) {
            // 为空，默认排序
            page.addOrder(items);
        }
        return page;
    }
}
