<template>
    <div class="tabs-wrap">
        <van-tabs v-model:active="visitorType" color="#1989fa" line-height="3px" animated swipeable>
            <van-tab title="访客预约" name="0" />
            <van-tab title="访客" name="1" />
        </van-tabs>
        <!-- 访客列表 -->
        <List :visitorType="visitorType" />
    </div>
</template>

<script>
import List from './blocks/list.vue';
export default {
    components: { List },
    data() {
        return {
            visitorType: "",
        };
    },
    created() {
        const visitorType = this.$route.query;
        this.visitorType = visitorType || '0';
    },
}
</script>
