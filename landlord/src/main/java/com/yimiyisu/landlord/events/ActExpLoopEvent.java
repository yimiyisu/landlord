package com.yimiyisu.landlord.events;

import com.google.common.eventbus.Subscribe;
import com.yimiyisu.landlord.domain.ArticleDo;
import com.yimiyisu.landlord.events.model.ActExpLoopEventModel;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.Crontab;
import com.zen.annotation.Inject;
import com.zen.interfaces.IEvent;
import com.zen.kit.DateKit;

import java.util.List;


@Crontab("0 */1 * * * ?")
public class ActExpLoopEvent implements IEvent<ActExpLoopEventModel> {

    @Inject
    private ZenEngine zenEngine;

    @Override
    @Subscribe
    public void execute(ActExpLoopEventModel actExpLoopEventModel) {
        // 查出近两天的所有活动
        String time = (DateKit.now() - 24 * 3600) + "";
        ZenResult activityListResult = zenEngine.execute("list/community_activity_loop", ZenData.create("type", "activity").put("createGmt", time));
        if (activityListResult.isEmpty()) return;
        List<ArticleDo> activityList = activityListResult.asList(ArticleDo.class);

        for (ArticleDo activity : activityList) {
            updateStatus(activity);
        }

    }

    // 状态更新
    private void updateStatus(ArticleDo activity) {
        long beginTime = activity.getBeginTime();
        long endTime = activity.getEndTime();
        long now = DateKit.now();

        if (now < beginTime) { // 未开始
            zenEngine.execute("patch/community_activity", ZenData.create("id", activity.getId()).put("status", 1));
        } else if (now > endTime) { // 已结束
            // 更新活动状态
            zenEngine.execute("patch/community_activity", ZenData.create("id", activity.getId()).put("status", 3));
            // 将报名该活动(状态为已报名)的记录该为已过期
            ZenResult actApplyResult = zenEngine.execute("list/act_apply_exp", ZenData.create("activityId", activity.getId()).put("status", 1));
            if (actApplyResult.isEmpty()) return;
            actApplyResult.asList(ArticleDo.class).forEach(act -> {
                zenEngine.execute("patch/act_apply", ZenData.create("id", act.getId()).put("status", 3));
            });
        } else { // 已开始
            zenEngine.execute("patch/community_activity", ZenData.create("id", activity.getId()).put("status", 2));
        }
    }
}
