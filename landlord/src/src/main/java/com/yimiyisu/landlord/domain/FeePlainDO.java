package com.yimiyisu.landlord.domain;

import lombok.Data;

import java.util.List;

@Data
public class FeePlainDO {
    private String id;
    private String creator;
    private long createGmt;
    private long updateGmt;

    /**
     * 费用配置名
     */
    private String title;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 费用类型
     */
    private String type;

    /**
     * 费率配置
     */
    private List<FeeConfig> configs;

    /**
     * 是否启用
     */
    private int status;

    /**
     * 默认费用(临时车费)
     */
    private long constant;
    private String communityId;

    @Data
    public static class FeeConfig {
        private String title;
        private long price;
        private String id;
    }
}
