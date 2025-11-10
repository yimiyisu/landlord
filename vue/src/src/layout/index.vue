<template>
    <div class="zenApp">
        <Topbar v-show="!showTab" />
        <div class="container" :style="bottom">
            <router-view />
        </div>
        <Footer v-if="!inMiniApp" v-show="showTab" />
    </div>
</template>
<script>
import { computed } from 'vue';
import Footer from "./footbar.vue";
import Topbar from "./topbar.vue";
const tabs = ['/welcome', '/bbs/index', '/member/index', '/activity/index']

export default {
    components: { Topbar, Footer },
    provide() {
        return { isBind: computed(() => this.isBind), houseId: computed(() => this.houseId) }
    },
    computed: {
        showTab() {
            const { path } = this.$route;
            return tabs.includes(path)
        },
        bottom() {
            return { paddingBottom: this.inMiniApp || !this.showTab ? '20px' : '50px' }
        }
    },
    data() {
        const inMiniApp = zen.env === "wechatMini"
        return {
            isBind: false,
            inMiniApp,
            houseId: ''
        }
    },

    async created() {
        try {
            const houseList = await $.get({ url: '/api/personal/myHouse' })
            if (houseList.length > 0) {
                this.isBind = true;
                this.houseId = houseList[0]
            }
        } catch (error) {
            console.log(error)
        }
    },
};
</script>
<style scoped>
.zenApp {
    min-height: 100vh;
    /* padding-bottom: 70px; */
    box-sizing: border-box;
    overflow: auto;
    background: var(--van-gray-1);
}
</style>
