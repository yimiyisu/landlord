<template>
    <!-- van-hairline--bottom -->
    <div class="item" @click="detail">
        <z-avatar :value="data.creator" :plain="false" class="nickname" />
        <div class="flex">
            <div class="flex-grow">
                <!-- 活动标题 -->
                <div class="activityname van-ellipsis" v-if="data.title">{{ data.title }}</div>
                <!-- 内容 -->
                <div class="content van-multi-ellipsis--l2">{{ data.content }}</div>
                <!-- 图片 -->
                <div class="imglist" >
                    <z-image v-if="data.image" :autoPreview="true" :modelValue="data.image" @click.stop/>
                </div>
                <!-- 发布时间 -->
                <div class="flex-aic">
                    <div class="publishtime"><z-date :value="data.createGmt" plain /></div>
                    <van-tag type="primary" v-if="data.tag">{{ data.tag }}</van-tag>
                </div>
            </div>
        </div>

        <div class="action">
            <div class="flex-jac flex-grow">
                <van-icon :name="likeStatus ? 'good-job' : 'good-job-o'" size="14" style="margin-right: 4px;"
                    @click.stop="handleLikeCollect('likes')" :color="likeStatus ? '#eb4d4b' : '#333'" />
                {{ data.likes }}
            </div>
            <div class="flex-jac flex-grow">
                <van-icon name="comment-o" size="14" style="margin-right: 4px;" />
                {{ data.comments }}
            </div>
        </div>
    </div>
</template>
<script>
export default {
    props: {
        type: String,
        data: Object
    },
    data() {
        return {
            defaultAvatar: 'https://ww2.sinaimg.cn/mw690/6f833727gy1htsvgzfbmpj218g18gdk0.jpg',
            defaultImage: 'https://pic1.zhimg.com/50/v2-8708ad1d062dc64b5dbec69ac1d35fd4_qhd.jpg?source=b6762063',
            defaultImage2: 'http://gips0.baidu.com/it/u=1490237218,4115737545&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720',
            imgList: [1, 2],
            likeStatus: false,//点赞状态
        }
    },
    created() {
        this.getLikeStatus()
    },
    methods: {
        detail() {
            this.$router.push({ path: `/bbs/detail`, query: { id: this.data.id, type: this.type } })
        },

        async getLikeStatus() {
            const res = await $.get({
                url: '/api/community/getArticleState',
                data: { articleId: this.data.id }
            })
            this.likeStatus = res.likes
        },

        handleLikeCollect(action) {
            this.likeStatus = !this.likeStatus
            if (this.likeStatus) {
                this.data.likes += 1
            } else {
                this.data.likes -= 1
            }
            $.post({
                url: '/api/community/handleLog',
                data: {
                    parent: this.data.id,
                    target: this.data.id,
                    action: action
                }
            })
        },
    },
}
</script>
<style scoped lang="scss">
.item {
    line-height: 1;
    background: var(--van-white);
    padding: 6px 12px;
    padding-bottom: 0;
    margin-bottom: 16px;
}

.avatar {
    margin-right: 10px;
}

:deep(.van-image--round) {

    height: 100%;
}

.nickname {
    font-size: 16px;
    color: rgba(91, 106, 145);
    font-weight: bold;
    padding-top: 4px;
    padding-bottom: 10px;
}

.activityname {
    color: #333;
    font-size: 16px;
    font-weight: bold;
    padding-bottom: 4px;
}

.content {
    font-size: 14px;
    color: #333;
    line-height: 18px;
}

.imglist {
    display: flex;
    padding-top: 8px;
    gap: 4px;
    flex-wrap: wrap;
}

.img-one {
    max-height: 200px;
    max-width: 280px;
}

.img-more {
    width: 90px;
    height: 90px;
    object-fit: cover;
}

:deep(.z-image .van-image) {
    width: 100%;
    height: 100%;
    object-fit: cover;
    aspect-ratio: 1 / 1;
}

.publishtime {
    color: #999;
    font-size: 12px;
    padding: 10px 0;
    margin-right: 10px;
}

.action {
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-top: 1px solid #f0f2f5;
    box-sizing: border-box;
    font-size: 14px;
    color: #444;
    line-height: 1;
}
</style>
