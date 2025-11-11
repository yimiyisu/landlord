package com.yimiyisu.landlord.domain;

import lombok.Data;

import java.util.List;

/**
 * @author Z-熙玉
 * @version 1.0
 */
@Data
public class ArticleDo {
    /**
     * 文章id
     */
    private String id;

    /**
     * 创作人
     */
    private String creator;

    /**
     * 创建时间
     */
    private long createGmt;

    /**
     * 更新时间
     */
    private long updateGmt;

    /**
     * 标题
     */
    private String title;

    /**
     * 开始时间
     */
    private long beginTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 活动地址
     */
    private String address;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 活动图片
     */
    private List<String> image;
    

    /**
     * 点赞数
     */
    private int likes;

    /**
     * 评论数
     */
    private int comments;

    /**
     * 收藏数
     */
    private int collection;

    /**
     * 贝壳数
     */
    private int shell;

    /**
     * 最近打赏记录
     */
    private List<TippingDO> tippingList;

    /**
     * 最近评论记录
     */
    private List<CommentDO> commentList;
}
