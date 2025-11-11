package com.yimiyisu.landlord.service;

import com.yimiyisu.landlord.domain.ArticleDo;
import com.yimiyisu.landlord.domain.CommentDO;
import com.yimiyisu.landlord.domain.TippingDO;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.Component;
import com.zen.annotation.Inject;
import com.zen.kit.JsonKit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Z-熙玉
 * @version 1.0
 */
@Component
public class CommunityService {

    @Inject
    private ZenEngine zenEngine;

    /**
     * 处理文章打赏
     * @param uid 打赏人
     * @param articleId 打赏文章
     * @param message 打赏留言
     * @param shell 打赏数量
     */
    public void handleArticleTipping(String uid, String articleId, int message, int shell) {
        // 创建打赏记录
        ZenResult putTippingResult = zenEngine.execute("put/tipping", ZenData.create()
                .put("uid", uid).put("message", message).put("articleId", articleId).put("shell", shell));
        Object data = putTippingResult.getData();
        TippingDO putTipping = JsonKit.parse(data, TippingDO.class);

        // 将打赏记录在 community_activity中(只记录最近5条)
        ZenResult articleResult = zenEngine.execute("get/community_activity", ZenData.create("id", articleId));
        ArticleDo article = articleResult.asEntity(ArticleDo.class);
        if (article.getTippingList() == null) {
            article.setTippingList(new ArrayList<>());
        }
        List<TippingDO> tippingList = article.getTippingList(); // 获取文章的打赏列表
        if (tippingList.size() == 5) tippingList.remove(0); // 如果超过5条，移除最旧的记录
        tippingList.add(putTipping); // 在末尾插入新记录

        // 更新文章的贝壳数以及最近的打赏记录
        zenEngine.execute("patch/community_activity", ZenData.create("id", articleId).put("tippingList", tippingList).put("shell", article.getShell() + shell));

        // 减少/增加被打赏人的贝壳数
        zenEngine.inc("member", article.getCreator(), "shell", shell);
        if (shell < 0) return; // shell < 0，为扣除贝壳操作，则不对打赏人的贝壳进行操作
        // 减少打赏人的贝壳
        zenEngine.inc("member", uid, "shell", -shell);
    }

    /**
     * 处理评论打赏
     * @param uid 打赏人
     * @param commentId 打赏评论
     * @param message 打赏留言
     * @param shell 打赏数量
     */
    public void handleCommentTipping(String uid, String commentId, int message, int shell) {
        // 创建打赏记录
        ZenResult putTippingResult = zenEngine.execute("put/tipping", ZenData.create()
                .put("uid", uid).put("message", message).put("commentId", commentId).put("shell", shell));
        Object data = putTippingResult.getData();
        TippingDO putTipping = JsonKit.parse(data, TippingDO.class);

        // 将打赏记录在 message中(只记录最近5条)
        ZenResult commentResult = zenEngine.execute("get/message", ZenData.create("id", commentId));
        CommentDO comment = commentResult.asEntity(CommentDO.class);
        if (comment.getTippingList() == null) {
            comment.setTippingList(new ArrayList<>());
        }
        List<TippingDO> tippingList = comment.getTippingList(); // 获取文章的打赏列表
        if (tippingList.size() == 5) tippingList.remove(0); // 如果超过5条，移除最旧的记录
        tippingList.add(putTipping); // 在末尾插入新记录

        // 更新评论的贝壳数以及最近的打赏记录
        zenEngine.execute("patch/message", ZenData.create("id", commentId).put("tippingList", tippingList).put("shell", comment.getShell() + shell));
        // 减少/增加被打赏人的贝壳数
        zenEngine.inc("member", comment.getCreator(), "shell", shell);
        if (shell < 0) return; // shell < 0，为扣除贝壳操作，则不对打赏人的贝壳进行操作
        // 减少打赏人的贝壳
        zenEngine.inc("member", uid, "shell", -shell);
    }
}
