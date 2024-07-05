package com.aurora.client.common.vo;

import lombok.Data;

import java.util.List;

/**
 * 概述VO
 */
@Data
public class ProfileVO {
    private String userId;

    private List<AllProfile> allProfiles;

    @Data
    public static class AllProfile {
        private String time;
        private List<ContentVO> profiles;
    }
}
