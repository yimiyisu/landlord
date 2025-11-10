<template>
    <van-steps direction="vertical" :active="paperLogList.length - 1">
        <van-step v-for="(item, index) in paperLogList" :key="index">
            <div class="log-item">{{ item.record }}</div>
            <div class="log-item">
                <z-image v-if="item.images && item.images.length > 0" :modelValue="item.images" readonly="true" />
            </div>
            <div class="log-item">处理人<z-user :value="item.uid" /></div>
            <div class="log-item">
                <z-date :value="item.createGmt" />
            </div>
        </van-step>
    </van-steps>
</template>

<script>
export default {
    props: {
        paperLogList: Array
    },
}
</script>

<style scoped>
.log-item {
    margin-bottom: 8px;
    /* 字段间的间距 */
}
/* 图片样式 */
.log-item z-image {
    display: block;
    /* 让图片独占一行 */
    margin: 0 auto;
    /* 图片水平居中 */
}
</style>
