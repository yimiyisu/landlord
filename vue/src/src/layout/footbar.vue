<template>
    <div>
        <van-tabbar route :active-color="activeColor" safe-area-inset-bottom inactive-color="#333">
            <van-tabbar-item replace to="/welcome">
                首页
                <template #icon="props">
                    <van-icon :name="props.active ? 'wap-home' : 'wap-home-o'" />
                </template>
            </van-tabbar-item>
            <van-tabbar-item replace to="/bbs/index">
                社区
                <template #icon="props">
                    <van-icon :name="props.active ? 'smile-comment' : 'smile-comment-o'" />
                </template>
            </van-tabbar-item>
            <van-tabbar-item v-if="isBind" @click="openLock">
                一键开门
                <template #icon>
                    <van-icon name="lock" />
                </template>
            </van-tabbar-item>
            <van-tabbar-item v-else to="/member/apply">
                待认证
                <template #icon>
                    <van-icon name="card" />
                </template>
            </van-tabbar-item>
            <van-tabbar-item replace to="/activity/index">
                消息
                <template #icon="props">
                    <van-icon :name="props.active ? 'notes' : 'notes-o'" />
                </template>
            </van-tabbar-item>
            <van-tabbar-item replace to="/member/index">
                我的
                <template #icon="props">
                    <van-icon :name="props.active ? 'manager' : 'manager-o'" />
                </template>
            </van-tabbar-item>
        </van-tabbar>
        <Locks v-model:show="visible" />
    </div>
</template>

<script>
import Locks from './locks.vue';
export default {
    name: "CommonNavBar",
    components: { Locks },
    inject: ['isBind'],
    data() {
        return {
            active: "home",
            visible: false,
            activeColor: zen.color || '#ee0a24'
        };
    },
    methods: {
        openLock() {
            this.visible = true
        },
    }
};
</script>
