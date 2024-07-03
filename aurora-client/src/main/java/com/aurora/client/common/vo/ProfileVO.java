package com.aurora.client.common.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 概述VO
 */
@Data
public class ProfileVO {
    private String userId;

    private Map<String, List<String>> allProfiles;
}
