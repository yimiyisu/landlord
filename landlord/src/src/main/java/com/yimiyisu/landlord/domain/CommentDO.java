package com.yimiyisu.landlord.domain;

import lombok.Data;

import java.util.List;

/**
 * @author Z-熙玉
 * @version 1.0
 * 评论留言DO
 */
@Data
public class CommentDO {
    /**
     * 评论id
     */
    private String id;

    /**
     * 活动、动态文章id
     */
    private String articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private long createGmt;

    /**
     * 评论人
     */
    private String creator;

    /**
     * 被回复的评论
     */
    private ReplyCommentDO replyContent;

    /**
     * 支持数
     */
    private int good;

    /**
     * 踩
     */
    private int stomp;

    /**
     * 贝壳数
     */
    private int shell;

    /**
     * 最近打赏记录
     */
    private List<TippingDO> tippingList;
}
