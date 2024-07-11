package com.aurora.client.common.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页VO
 */
@Data
public class PageVO<T> {

    private Long total;
    private Long pages;
    private List<T> list;

    public static <PO, VO> PageVO<VO> of(Page<PO> p, Function<PO, VO> convertor) {
        PageVO<VO> pvo = new PageVO<>();
        // 1.总条数
        pvo.setTotal(p.getTotal());
        // 2.总页数
        pvo.setPages(p.getPages());
        // 3.当前页数据
        List<PO> records = p.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            pvo.setList(Collections.emptyList());
            return pvo;
        }
        // 4.拷贝user的VO
        pvo.setList(records.stream().map(convertor).collect(Collectors.toList()));
        // 5.返回
        return pvo;
    }
}
