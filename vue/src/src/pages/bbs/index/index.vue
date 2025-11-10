<template>
    <div>
        <van-tabs v-model:active="active" shrink animated sticky @click-tab="change">
            <van-tab title="活动" name="activity"></van-tab>
            <van-tab title="动态" name="latest"></van-tab>
            <van-tab title="集市" name="market"></van-tab>
        </van-tabs>
        <z-block url="/do/list/community_activity" :params="params">
            <template #default="data">
                <Item v-for="item in data" :key="item.id" :data="item" :type="active" />
                <Post :type="active" />
            </template>
        </z-block>
    </div>
</template>

<script>
import Item from './blocks/item.vue';
import Post from './blocks/post';
export default {
    components: { Item, Post },
    computed: {
        params() {
            return { type: this.active }
        }
    },
    data() {
        return {
            active: this.getInitialActive(),
        };
    },
    methods: {
        // 获取初始active值
        getInitialActive() {
            const urlActive = this.$route.query.tab
            if (urlActive) {
                return urlActive
            }
            const savedActive = localStorage.getItem('bbs_active_tab')
            if (savedActive) {
                return savedActive
            }
            return 'activity'
        },

        // 切换tab
        change(tab) {
            this.active = tab.name
            localStorage.setItem('bbs_active_tab', tab.name)
        },
    },
    
    mounted() {
        this.$watch('$route', (newRoute) => {
            const urlActive = newRoute.query.tab
            if (urlActive && urlActive !== this.active) {
                this.active = urlActive
            }
        })
    }
};
</script>
<style lang="scss" scoped>
:deep(.van-tab--active) {
    font-size: 22px;
}


.tags {
    margin: 8px 0;

    .van-tag {
        margin-left: 8px;
    }
}


.van-tabs {
    --van-tab-font-size: 16px;
    --van-padding-xs: 14px;
}
</style>
