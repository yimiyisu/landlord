<template>
    <div>
        <div class="flex-jasc">
            <div class="commenttitle">{{ detailInfo.comments }}条留言</div>
        </div>
        <div class="flex-aic postbtn">
            <!-- <z-avatar :value="detailInfo.creator" :plain="false" /> -->
            <div class="emptyinput flex-aic flex-grow" @click.stop="handleReplyClick(null)">看对眼就留言，问问更多细节</div>
        </div>

        <z-list ref="commentList" url="/do/select/message" :size="5" :params="params" emptyText="暂无评论" @refresh="refresh">
            <template #row="item">
                <div class="flex commentitem">
                    <div class="flex-grow">
                        <div class="flex-jasc topbox">
                            <div class="flex-aic">
                                <z-avatar class="avatar" :value="item.creator" :plain="false" />
                                <div class="time"><z-date :value="item.createGmt" plain /></div>
                            </div>
                            <van-icon @click.stop="handleReplyClick(item)" name="comment-o" size="12" color="#999" />
                        </div>
                        <div class="content">
                            {{ item.content }}
                            <div v-if="item.replyContent" class="reply">
                                回复 <z-user :value="item.replyContent.uid" />
                                :{{ item.replyContent.content }}
                            </div>
                            <!-- 打赏 -->
                            <Shell :shells="item.tippingList" :commentId="item.id" />
                        </div>
                        <!-- 点赞或踩 -->
                        <div style="margin-top: 8px; border-bottom: 1px solid #f0f2f5;">
                            <van-row>
                                <van-col span="4">
                                    <div>
                                        <van-icon :name="hasAction('good', item.id) ? 'good-job' : 'good-job-o'"
                                            :color="hasAction('good', item.id) ? '#eb4d4b' : '#999'"
                                            @click="handleSupport('good', item.id)" />
                                        {{ item.good }}
                                    </div>
                                </van-col>
                                <van-col span="4">
                                    <div>
                                        <van-icon :name="hasAction('stomp', item.id) ? 'good-job' : 'good-job-o'"
                                            :color="hasAction('stomp', item.id) ? '#c0c0c0' : '#999'"
                                            class="thumbs-down" @click="handleSupport('stomp', item.id)" />
                                        {{ item.stomp }}
                                    </div>
                                </van-col>
                                <van-col span="20"></van-col>
                            </van-row>
                        </div>
                    </div>
                </div>
            </template>
        </z-list>
    </div>
</template>

<script>
import Shell from '../shell/shell.vue';
export default {
    name: 'Comment',
    components: { Shell },
    computed: {
        params() {
            return { articleId: this.$route.query.id }
        }
    },
    props: {
        commentLog: Object,
        detailInfo: Object
    },
    data() {
        return {

        }
    },
    methods: {
        handleReplyClick(item) {
            this.$emit('showPost', item)
        },
        // 评论点赞或踩
        handleSupport(action, id) {
            $.post({
                url: '/api/community/handleLog',
                data: {
                    parent: this.$route.query.id,
                    target: id,
                    action: action
                }
            }).then(() => {
                this.$refs.commentList.refresh()
                this.refresh()
            })
        },
        // 判断是否点赞或踩
        hasAction(action, targetId) {
            const goodLogs = this.commentLog.good
            const stompLogs = this.commentLog.stomp
            if (action === 'good') {
                return goodLogs.some(log =>
                    log.target === targetId && log.action === action
                )
            } else {
                return stompLogs.some(log =>
                    log.target === targetId && log.action === action
                )
            }
        },

        refresh() {
            this.$refs.commentList.refresh()
            this.$emit('refresh')
        }
    }
}
</script>

<style lang="scss" scoped>
.postbtn {
    padding-bottom: 20px;

    .emptyinput {
        margin-left: 10px;
        font-size: 12px;
        color: #999;
        width: 90%;
        padding-left: 20px;
        box-sizing: border-box;
        height: 30px;
        background-color: #eee;
        border-radius: 30px;
    }
}

.commenttitle {
    padding: 14px 0;
    font-weight: bold;
    color: #333;
}

:deep(.van-image--round) {
    height: 100%;
}
.commentitem {
    padding-bottom: 8px;
    line-height: 1;

    .avatar {
        margin-right: 6px;
        color: #9B9B9B;
        font-size: 12px;
        font-weight: bold;
        box-sizing: border-box;
    }

    .topbox {
        padding-top: 2px;
        padding-bottom: 4px;
    }

    .time {
        color: #999;
        font-size: 12px;
        padding-left: 10px;
    }

    .content {
        color: #333;
        font-size: 12px;
        line-height: 18px;
    }

    .reply {
        color: #999;
        background-color: #eee;
        border-radius: 5px 5px 0 0;
    }
}

.thumbs-down {
    transform: rotate(180deg);
    display: inline-block;
    transform-origin: center;
}
</style>
