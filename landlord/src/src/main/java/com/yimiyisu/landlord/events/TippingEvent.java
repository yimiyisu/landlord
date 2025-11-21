package com.yimiyisu.landlord.events;

import com.google.common.eventbus.Subscribe;
import com.yimiyisu.landlord.events.model.TippingEventModel;
import com.yimiyisu.landlord.service.CommunityService;
import com.zen.annotation.Inject;
import com.zen.interfaces.IEvent;

/**
 * @author Z-熙玉
 * @version 1.0
 * 打赏异步事件
 */
public class TippingEvent implements IEvent<TippingEventModel> {

    @Inject
    private CommunityService communityService;

    @Override
    @Subscribe
    public void execute(TippingEventModel tippingEventModel) {
        String uid = tippingEventModel.getUid(); // 赠送人
        int message = tippingEventModel.getMessage(); // 赠送留言
        int shell = tippingEventModel.getShell(); // 赠送贝壳数
        String articleId = tippingEventModel.getArticleId();
        String commentId = tippingEventModel.getCommentId();

        // 处理文章打赏
        if (articleId != null) communityService.handleArticleTipping(uid, articleId, message, shell);
        else // 处理评论打赏
            if (commentId != null) communityService.handleCommentTipping(uid, commentId, message, shell);
    }
}
