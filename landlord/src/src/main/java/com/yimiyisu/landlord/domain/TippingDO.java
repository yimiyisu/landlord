package com.yimiyisu.landlord.domain;

import lombok.Data;

/**
 * @author Z-熙玉
 * @version 1.0
 */
@Data
public class TippingDO {
    private String id;
    private String uid;
    private String articleId;
    private int shell;
    private int message;
}
