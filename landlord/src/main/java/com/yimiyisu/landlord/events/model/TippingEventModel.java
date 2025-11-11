package com.yimiyisu.landlord.events.model;

import lombok.Data;

/**
 * @author Z-熙玉
 * @version 1.0
 */
@Data
public class TippingEventModel {
    private String articleId;
    private String uid;
    private int shell; // 贝壳数
    private int message; // 打赏留言
    private String commentId;
    private int type;
}
