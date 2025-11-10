<template>
    <div class="page">
        <!-- 文章详情 -->
        <Article v-if="detailInfo.type === 'latest'" :detailInfo="detailInfo" />
        <Activity v-if="detailInfo.type === 'activity'" :detailInfo="detailInfo" />
        <Market v-if="detailInfo.type === 'market'" :detailInfo="detailInfo" />

        <!-- action -->
        <Action :data="detailInfo" :type="type" :state="state" @showPost="showPost" @getDetailInfo="refreshAfterTipping"></Action>

        <!-- 评论记录 -->
        <Comment ref="commentList" :detailInfo="detailInfo" @showPost="showPost" :commentLog="commentLog" @refresh="refreshAfterTipping"></Comment>

        <!-- 文章打赏记录 -->
        <ShellLog :detailInfo="detailInfo" />
        <!-- 新增评论 -->
        <CommentPost ref="commentPost" v-model:visible="show_post" :parentComment="parentComment" @getDetailInfo="getDetailInfo"></CommentPost>
    </div>
</template>

<script>
import Action from './blocks/action/action.vue'; 
import CommentPost from './blocks/action/commentPost.vue';
import Comment from './blocks/comment/comment.vue';
import Activity from './blocks/content/activity.vue';
import Article from './blocks/content/article.vue';
import Market from './blocks/content/market.vue';
import ShellLog from './blocks/shell/shellLog.vue';
export default {
    components: {
        Market,
        Article,
        Activity,
        Comment,
        CommentPost,
        Action,
        ShellLog
    },
    data() {
        return {
            type: '',
            show_post: false,
            parentComment: null,
            tippingComment: null,
            detailInfo: {},
            commentList: [],
            state: {},
            commentLog: {
                good: [],
                stomp: []
            }
        };
    },
    async created() {
        this.type = this.$route.query.type
        this.getDetail()
        //获取文章详情
        this.getDetailInfo()
    },
    methods: {
        async getDetailInfo() {
            this.detailInfo = await $.get({
                url: "/do/get/community_activity",
                data: { id: this.$route.query.id }
            })
        },
        async getDetail() {
            const articleId = this.$route.query.id

            // 获取文章状态
            this.state = await $.get({
                url: "/api/community/getArticleState",
                data: { articleId: articleId }
            })
            // 获取所有评论的日志信息
            this.commentLog = await $.get({
                url: "/api/community/getComments",
                data: { articleId: this.$route.query.id }
            })
        },

        // 评论
        showPost(comment) {
            this.parentComment = comment
            this.show_post = true
        },
        
        async refreshAfterTipping() {
            await this.getDetailInfo()
            await this.getDetail()
        },
    }
};
</script>
<style lang="scss" scoped>
.page {
    min-height: 100vh;
    background-color: #fff;
    padding: 16px;
    box-sizing: border-box;
}

.user {
    .avatar {
        margin-right: 10px;
    }

    .nickname {
        font-size: 16px;
        color: rgba(91, 106, 145);
        font-weight: bold;
        padding: 3px 0;
    }

    .desc {
        color: #999;
        font-size: 12px;
    }
}

:deep(.van-button--small) {
    height: 24px;
}
</style>
