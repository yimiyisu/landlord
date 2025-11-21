package com.yimiyisu.landlord.events.model;

import lombok.Data;

/**
 * @author Z-熙玉
 * @version 1.0
 */
@Data
public class CommunityLogEventModel {
    private String parent;
    private String target;
    private String uid;
    private String action;
}
