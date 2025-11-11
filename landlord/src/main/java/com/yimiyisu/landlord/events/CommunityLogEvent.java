package com.yimiyisu.landlord.events;

import com.google.common.eventbus.Subscribe;
import com.yimiyisu.landlord.events.model.CommunityLogEventModel;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.Inject;
import com.zen.interfaces.IEvent;
import com.zen.kit.StringKit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Z-熙玉
 * @version 1.0
 * 社区日志异步
 */
@Slf4j
public class CommunityLogEvent implements IEvent<CommunityLogEventModel> {

    @Inject
    private ZenEngine zenEngine;

    @Override
    @Subscribe
    public void execute(CommunityLogEventModel communityLogEventModel) {
        ZenData handleData = ZenData.create()
                .put("uid", communityLogEventModel.getUid())
                .put("target", communityLogEventModel.getTarget())
                .put("action", communityLogEventModel.getAction())
                .put("parent", communityLogEventModel.getParent());
        ZenResult logResult = zenEngine.execute("get/shequ_log_handle", handleData);

        if (logResult.isEmpty()) { // 无操作记录，则为点赞或收藏
            zenEngine.execute("put/shequ_log", handleData.put("creator", communityLogEventModel.getUid()));
            if (communityLogEventModel.getParent().equals(communityLogEventModel.getTarget())) { // 对文章的操作
                zenEngine.inc("community_activity", communityLogEventModel.getTarget(), communityLogEventModel.getAction(), 1);
            } else { // 对评论的操作
                zenEngine.inc("message", communityLogEventModel.getTarget(), communityLogEventModel.getAction(), 1);
            }
        } else { // 有操作记录，则为取消点赞收藏等操作
            zenEngine.execute("delete/shequ_log", ZenData.create("id", logResult.get("id")));
            if (communityLogEventModel.getParent().equals(communityLogEventModel.getTarget())) { // 对文章的操作
                zenEngine.inc("community_activity", communityLogEventModel.getTarget(), communityLogEventModel.getAction(), -1);
            } else { // 对评论的操作
                zenEngine.inc("message", communityLogEventModel.getTarget(), communityLogEventModel.getAction(), -1);
            }
        }
    }
}
