package com.yimiyisu.landlord.hooks;

import com.yimiyisu.landlord.events.model.CommentEventModel;
import com.zen.ZenData;
import com.zen.ZenResult;
import com.zen.annotation.ZenHook;
import com.zen.interfaces.IHook;
import com.zen.kit.EventKit;

/**
 * @author Z-熙玉
 * @version 1.0
 * 拦截新增评论 - 发异步计算评论数
 */
@ZenHook("put/message")
public class CommentPutHook implements IHook {

    @Override
    public void after(ZenData data, ZenResult result) {
        String articleId = data.get("articleId");
        CommentEventModel commentEventModel = new CommentEventModel();
        commentEventModel.setArticleId(articleId);
        EventKit.trigger(commentEventModel);
        IHook.super.after(data, result);
    }
}
