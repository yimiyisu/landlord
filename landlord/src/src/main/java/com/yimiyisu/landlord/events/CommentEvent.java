package com.yimiyisu.landlord.events;

import com.google.common.eventbus.Subscribe;
import com.yimiyisu.landlord.events.model.CommentEventModel;
import com.zen.ZenEngine;
import com.zen.annotation.Inject;
import com.zen.interfaces.IEvent;

/**
 * @author Z-熙玉
 * @version 1.0
 * 计算评论数
 */
public class CommentEvent implements IEvent<CommentEventModel> {

    @Inject
    private ZenEngine zenEngine;

    @Override
    @Subscribe
    public void execute(CommentEventModel commentEventModel) {
        String articleId = commentEventModel.getArticleId();
        zenEngine.inc("community_activity", articleId, "comments", 1);
    }
}
