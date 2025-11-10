<template>
    <div class="notice">
        <h1>小区公告</h1>
        <z-list url="/do/select/announcement" :size="10" emptyText="暂无公告">
            <template #row="formData">
                <div class="article van-hairline--bottom">
                    <div class="row header">
                        <div class="title">{{ formData.title }}</div>
                        <z-dict v-model="formData.type" code="announcementType" class="type" />
                    </div>

                    <div class="content" v-html="formData.content"></div>
                    <div class="imglist" v-if="formData.image">
                        <z-image :autoPreview="true" :modelValue="imageList(formData.image)" @click.stop />
                    </div>

                    <div class="row footer">
                        <div class="user">
                            <z-user :value="formData.creator" readonly />
                        </div>
                        <div class="time">
                            <z-date :value="formData.createGmt" />
                        </div>
                    </div>
                </div>
            </template>
        </z-list>
    </div>
</template>

<script>
export default {
    name: 'Notice',
    methods: {
        imageList(data) {
            let images = [];
            if (data) {
                data.forEach(item => {
                    images.push(item.url);
                })
            }
            return images;
        }
    },
}
</script>

<style lang="scss" scoped>
h1 {
    color: var(--van-black);
    font-size: 16px;
    margin-left: -12px;
    padding-left: 10px;
    border-left: 3px solid var(--van-primary-color);
}

.article {
    padding: 12px 0;
    border-bottom: 1px solid var(--van-gray-3);
}

.notice {
    background: var(--van-white);
    padding: 6px 12px;
}

/* 行布局 */
.row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

/* 第一行样式 */
.header {
    .title {
        color: var(--van-black);
        font-size: 18px;
        font-weight: 500;
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 10px;
    }

    .type {
        font-size: 16px;
        white-space: nowrap;
    }
}

/* 内容样式 */
.content {
    font-size: 15px;
    margin-bottom: 8px;
}

/* 图片样式 */
.imglist {
    display: flex;
    padding-top: 8px;
    gap: 4px;
    flex-wrap: wrap;
    margin-bottom: 20px;
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

/* 底部样式 */
.footer {
    margin-bottom: 0;

    .user {
        flex: 1;
    }

    .time {
        color: var(--van-gray-5);
    }
}
</style>
