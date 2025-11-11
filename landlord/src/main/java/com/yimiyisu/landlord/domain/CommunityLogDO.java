package com.yimiyisu.landlord.domain;

import lombok.Data;

/**
 * @author Z-熙玉
 * @version 1.0
 * 点赞收藏DO
 */
@Data
public class CommunityLogDO {
    private String id;
    private String uid;
    private String parent;
    private String target;
    private String action;
}
