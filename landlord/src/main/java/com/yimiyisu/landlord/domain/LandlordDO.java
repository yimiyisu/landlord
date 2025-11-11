package com.yimiyisu.landlord.domain;

import lombok.Data;

/**
 * 业主DO
 */
@Data
public class LandlordDO {
    /**
     * id
     */
    private String id;

    /**
     * 社区id
     */
    private String communityId;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 姓名
     */
    private String title;

    /**
     * 是否启用
     */
    private int isEnabled;

    /**
     * 身份证
     */
    private String idcard;
}
