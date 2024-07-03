package com.aurora.client.common.vo;

import com.aurora.client.utils.AuroraStringUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 分页VO
 */
@Data
public class PageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNumber = 1;

    private Integer pageSize = 10;

    private String sort;

    private String order;

    private Boolean notConvert;

    public String getSort() {
        if (StringUtils.isNotEmpty(sort)) {
            if (notConvert == null || Boolean.FALSE.equals(notConvert)) {
                return AuroraStringUtils.camel2Underline(sort);
            } else {
                return sort;
            }
        }
        return sort;
    }
}
