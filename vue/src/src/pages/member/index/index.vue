<template>
    <div>
        <Card />
        <div class="wrap">
            <van-grid :column-num="4" class="service-grid">
                <van-grid-item v-for="(link, idx) in links" :key="idx" :icon="link.icon" :text="link.label"
                    @click="jump(link.href)" />
            </van-grid>
            <van-cell-group title="我的">
                <van-cell title="我的工单" is-link to="/member/paper" />
                <van-cell title="我的服务" is-link />
                <van-cell title="我的订单" is-link />
                <van-cell title="我的宠物" is-link to="/member/pet" />
                <van-cell title="我的收藏" is-link />
                <van-cell title="我的车位" is-link to="/member/parking" />
                <van-cell title="我的账单" is-link to="/member/bills" />
                <van-cell title="在线申请" is-link to="/member/onlineApply" />
            </van-cell-group>
            <van-cell-group title="其他">
                <van-cell title="设置" is-link />
                <van-cell title="关于我们" is-link />
                <van-cell title="邀请好友" is-link />
            </van-cell-group>
        </div>
    </div>
</template>

<script>
import Card from './card.vue';
export default {
    components: { Card },
    data() {
        return {
            links: [
                { icon: 'wap-home-o', href: '', label: '我的房屋' },
                { icon: 'gold-coin-o', href: '', label: '积分中心' },
                { icon: 'gift-card-o', href: '', label: '卡券中心' },
                { icon: 'cluster-o', href: '/member/activity', label: '我的活动' }
            ],
        };
    },
    created() { },
    methods: {
        jump(href) {
            this.$router.push(href)
        }
    },
};
</script>
<style lang="scss" scoped>
.wrap {
    border-radius: 8px;
    margin: 0 16px;
}
</style>
