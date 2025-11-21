<template>
    <div class="tabs-wrap">
        <van-tabs v-model:active="activeName"  @click-tab="onClickTab">
            <van-tab title="访客预约" name="visitorType0" />
            <van-tab title="访客" name="visitor" />
        </van-tabs>
        <List v-if="activeName === 'visitorType0'" :actionShow="actionShow" />
        <VisitorList v-else />
    </div>
</template>

<script>
import List from './blocks/list.vue';
import VisitorList from './blocks/visitorList.vue';
export default {
    components: { List, VisitorList },
    data() {
        return {
            activeName: 'visitorType0',
            actionShow: true
        };
    },
    created() {
        const visitorType = this.$route.query;
        this.visitorType = visitorType || '0';
    },
    methods: {
        onClickTab(tab) {
            if (tab.name === 'visitorType0') {
                this.actionShow = true;
            } else {
                this.actionShow = false;
            }
        }
    }
}
</script>
