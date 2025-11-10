<template>
    <CreatorInfo :detailInfo="detailInfo" />

    <div class="activity">
        <div class="tipitem flex">
            <div class="flex-aic">
                <van-icon name="clock-o" color="#999" size="12" />
                <div class="tipitem-label">时间</div>
            </div>
            <div class="flex-grow">
                <z-date :value="detailInfo.beginTime" />-
                <z-date :value="detailInfo.endTime" />
            </div>
        </div>
        <div class="tipitem flex">
            <div class="flex-aic">
                <van-icon name="location-o" color="#999" size="12" />
                <div class="tipitem-label">地点</div>
            </div>
            <div class="flex-grow">{{ detailInfo.address }}</div>
        </div>
        <div class="tipitem flex">
            <div class="flex-aic">
                <van-icon name="balance-o" color="#999" size="12" />
                <div class="tipitem-label">费用</div>
            </div>
            <div class="flex-grow">
                <z-money :modelValue="detailInfo.amount ? detailInfo.amount : '0'" />
            </div>
        </div>
        <div class="title">
            <div class="main">
                <div class="title">{{ detailInfo.title }}</div>
                <div class="content">{{ detailInfo.content }}</div>
                <div class="imglist">
                    <z-image v-if="detailInfo.image" :modelValue="detailInfo.image" />
                </div>
            </div>
        </div>
    </div>

</template>
<script>
import CreatorInfo from './creatorInfo.vue';
export default {
    components: {
        CreatorInfo
    },
    props: {
        detailInfo: Object
    },
    created() {
        this.detailInfo.address = JSON.parse(this.detailInfo.address).label
    }
}
</script>
<style scoped lang="scss">
.activity {
    padding-top: 14px;
    line-height: 1;

    .tipitem {
        color: #333;
        font-size: 12px;
        line-height: 18px;
        padding-bottom: 6px;
        align-items: start;

        &:last-child {
            padding-bottom: 0;
        }

        &-label {
            color: #999;
            padding-right: 10px;
            padding-left: 4px;
        }
    }
}

.main {
    .title {
        font-size: 16px;
        line-height: 20px;
        font-weight: bold;
        color: #333;
        margin-top: 10px;
    }

    .content {
        font-size: 14px;
        color: #333;
        line-height: 20px;
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .imglist {
        overflow: hidden;
        border-radius: 10px;
    }

    .img {
        display: block;
        margin-bottom: 2px;

        &:last-child {
            margin-bottom: 0;
        }
    }
}

:deep(.z-image .van-image) {
    width: 100%;
    height: 100%;
    object-fit: cover;
    aspect-ratio: 1 / 1;
}
</style>
