<template>
    <van-cell-group>
        <van-cell title="工单号" :value="paperInfo.id" />
        <van-cell title="房屋" v-if="paperInfo.houseId" >
            <z-text v-model="paperInfo.houseId" depend="house" />
        </van-cell>
        <van-cell title="车牌号" v-if="paperInfo.licensePlate" :value="paperInfo.licensePlate" />
        <van-cell title="问题" :value="paperInfo.describe" />
        <div class="imglist" v-if="paperInfo.images && paperInfo.images.length > 0">
            <div class="image-title">图片 </div>
            <z-image  :modelValue="paperInfo.images" readonly="true" />
        </div>
        <van-divider :style="{ color: '#1989fa',  padding: '0 16px', margin: '0' }"/>
        <van-cell title="当前处理人" :value="paperInfo.recipientId">
            <template #value>
                <z-text v-model="paperInfo.recipientId" depend="community_manager" v-if="paperInfo?.recipientId" />
                <div v-else>暂无</div>
            </template>
        </van-cell>
        <van-cell title="创建时间">
            <z-date :value="paperInfo.createGmt" />
        </van-cell>
    </van-cell-group>
</template>

<script>
export default {
    props: {
        paperInfo: Object
    },
}
</script>
<style scoped>
.imglist {
    margin: 15px 15px;

    .image-title {
        font-size: 14px;
        margin-bottom: 5px;
    }
}
</style>
