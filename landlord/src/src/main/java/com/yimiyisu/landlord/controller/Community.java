package com.yimiyisu.landlord.controller;

import com.yimiyisu.landlord.domain.CommentDO;
import com.yimiyisu.landlord.domain.CommunityLogDO;
import com.yimiyisu.landlord.events.model.CommunityLogEventModel;
import com.yimiyisu.landlord.events.model.TippingEventModel;
import com.zen.ZenController;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.AccessRole;
import com.zen.annotation.Inject;
import com.zen.enums.ZenRole;
import com.zen.kit.EventKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Z-熙玉
 * @version 1.0
 * 评论留言
 */
@AccessRole(ZenRole.SIGNATURE)
public class Community extends ZenController {

    // 最少贝壳数
    private static final int MIN_SHELL = -500;

    @Inject
    private ZenEngine zenEngine;

    // 查询评论列表
    public ZenResult getCommentList(ZenData data) {
        // 获取所有评论数据
        List<CommentDO> comments = new ArrayList<>();
        String articleId = data.get("articleId");
        int currentPage = Integer.parseInt(data.get("currentPage"));
        ZenResult commentResult = zenEngine.execute("list/message", ZenData.create("articleId", articleId).put("page", currentPage - 1).put("pageSize", 10));
        if (commentResult.isEmpty()) {
            return ZenResult.success().setData(comments);
        }
        comments = commentResult.asList(CommentDO.class);
        return ZenResult.success().setData(comments);
    }

    // 异步处理点赞或收藏
    public ZenResult handleLog(ZenData data) {
        String uid = data.getUid();
        CommunityLogEventModel logEventModel = data.parse(CommunityLogEventModel.class);
        logEventModel.setUid(uid);
        EventKit.trigger(logEventModel);
        return ZenResult.success();
    }

    // 查询文章日志信息 - 判断用户对文章的点赞和收藏情况
    public ZenResult getArticleState(ZenData data) {
        String articleId = data.get("articleId");
        String uid = data.getUid();
        ZenData queryData = ZenData.create().put("target", articleId).put("uid", uid);
        ZenResult likeResult = zenEngine.execute("get/shequ_log_state", queryData.put("action", "likes"));
        ZenResult collectionResult = zenEngine.execute("get/shequ_log_state", queryData.put("action", "collection"));

        Map<String, Boolean> resultMap = new HashMap<>();
        if (likeResult.isEmpty()) {
            resultMap.put("likes", false);
        } else {
            resultMap.put("likes", true);
        }
        if (collectionResult.isEmpty()) {
            resultMap.put("collection", false);
        } else {
            resultMap.put("collection", true);
        }
        return ZenResult.success().setData(resultMap);
    }

    // 查询某篇文章评论的日志信息
    public ZenResult getComments(ZenData data) {
        String articleId = data.get("articleId");
        String uid = data.getUid();
        List<CommunityLogDO> commentGoodLogs = new ArrayList<>();
        List<CommunityLogDO> commentStompLogs = new ArrayList<>();
        Map<String, List<CommunityLogDO>> resultMap = new HashMap<>();
        getAllCommentLog(0, uid, articleId, "good", commentGoodLogs);
        getAllCommentLog(0, uid, articleId, "stomp", commentStompLogs);
        resultMap.put("good", commentGoodLogs);
        resultMap.put("stomp", commentStompLogs);
        return ZenResult.success().setData(resultMap);
    }

    // 关注或取消关注
    public ZenResult concerned(ZenData data) {
        String concernedUid = data.get("concerned"); // 被关注人uid
        String uid = data.getUid();
        Boolean action = data.get("action", Boolean.class);

        if (action) { // 执行关注操作 - 在关注表中插入数据
            zenEngine.execute("put/concern", ZenData.create().put("concernedUid", concernedUid).put("concernUid", uid));
        } else { // 取关 - 删除数据
            ZenResult concernResult = zenEngine.execute("get/concern", ZenData.create().put("concernedUid", concernedUid).put("concernUid", uid));
            String id = concernResult.get("id");
            zenEngine.execute("delete/concern", ZenData.create().put("id", id));
        }
        return ZenResult.success();
    }

    // 异步打赏
    public ZenResult tipping(ZenData data) {
        TippingEventModel tippingEventModel = data.parse(TippingEventModel.class);
        String uid = data.getUid();

        // 检验贝壳数是否足够
        if (tippingEventModel.getType() == 1) { // 赠送
            ZenResult memberResult = zenEngine.execute("get/member", ZenData.create().put("uid", uid));
            int shell = (int) Double.parseDouble(memberResult.get("shell"));
            if (shell == 0 || shell < tippingEventModel.getShell()) {
                return ZenResult.success("贝壳数不足无法赠送");
            }

        } else if (tippingEventModel.getType() == 0) { // 扣除
            ZenResult articleResult = zenEngine.execute("get/community_activity", ZenData.create("id", tippingEventModel.getArticleId()));
            String creator = articleResult.get("creator");
            ZenResult memberResult = zenEngine.execute("get/member", ZenData.create().put("uid", creator));
            int shell = (int) Double.parseDouble(memberResult.get("shell"));
            if (shell <= MIN_SHELL) { // 低于最低限度，无法扣除
                return ZenResult.success("低于最低限度，无法扣除");
            }
            tippingEventModel.setShell(-tippingEventModel.getShell());
        }

        // 发送异步请求赠送贝壳
        tippingEventModel.setUid(uid);
        EventKit.trigger(tippingEventModel);
        return ZenResult.success();
    }

    // 递归获取文章所有评论的日志信息
    private void getAllCommentLog(Integer page, String uid, String articleId, String action, List<CommunityLogDO> commentLog) {
        ZenResult result = zenEngine.execute("list/shequ_commentlog", ZenData.create().put("page", page).put("parent", articleId).put("action", action).put("uid", uid));
        page = page + 1;
        if (result == null || result.isEmpty()) return;
        List<CommunityLogDO> logs = result.asList(CommunityLogDO.class);
        commentLog.addAll(logs);
        this.getAllCommentLog(page, uid, articleId, action, commentLog);
    }

}
