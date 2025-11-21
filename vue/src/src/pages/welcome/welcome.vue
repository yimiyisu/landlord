<template>
    <ComunityList v-if="showArea" @finish="finish" />
    <div v-else>
        <van-swipe :height="260" :autoplay="3000">
            <van-swipe-item v-for="image in images" :key="image">
                <van-image height="100%" fit="cover" position="left" :src="image" />
            </van-swipe-item>
        </van-swipe>
        <van-grid class="quickNavi" square>
            <van-grid-item icon="phone-o" text="联系物业" />
            <van-grid-item icon="records-o" text="维修上报" to="member/paper" />
            <van-grid-item @click="camera" icon="logistics" text="违章抓拍" />
            <van-grid-item @click="scan" icon="aim" text="活动中心" />
            <van-grid-item icon="after-sale" text="缴费清单" to="member/bills" />
            <van-grid-item icon="user-circle-o" text="访客预约" to="member/visitor" />
            <van-grid-item icon="flower-o" text="在线申请" to="member/onlineApply" />
            <van-grid-item icon="newspaper-o" text="在线社区" />
        </van-grid>
        <Notice />
        <z-action label="举报" size="small" :fields="reportFields" :data="initData" url="/do/put/workorder" ref="action"
            @beforeShow="beforeShow" type="bubble" :beforeSubmit="beforeSubmit" />
    </div>
</template>
<script>
import CarInfo from './blocks/carInfo.vue';
import ComunityList from './blocks/comunityList.vue';
import Notice from './blocks/notice.vue';
export default {
    inheritAttrs: false,
    components: { Notice, ComunityList },
    data() {
        return {
            showArea: false,
            images: [
                'https://b.ebus.vip/banner/b1.jpg',
                'https://b.ebus.vip/banner/b3.jpg',
                'https://b.ebus.vip/banner/b4.jpg',
                'https://b.ebus.vip/banner/b5.jpg',
            ],
            initData: {},
            reportFields: [
                { name: 'info', label: 'ccc', component: CarInfo },
                { name: 'type', label: '举报类型', code: 'workorderTypeMobile', default: 1 },
                { name: 'licensePlate', label: '举报车牌号', type: 'input', visible:(formData)=>formData.type != 1 },
                { name: 'houseId', label: '举报房屋', depend:'house', tenant:'communityId',visible:(formData)=>formData.type == 1 },
                { name: 'describe', label: '举报描述', type: 'textarea' },
                { name: 'phone', label: '联系电话', type: 'tel' },
                { name: 'file', label: '附件', type: 'attach' },
            ]
        }
    },
    async created() {
        const token = await $.post({url:"/api/home/trialLogin"});
        if(token && $.token()) {
            $.token(token)
        }
        const tenantId = $.tenant()
        if (window.tenant) {
            $.tenant(window.tenant)
            return this.showArea = false
        }
        if (!tenantId) {
            return this.showArea = true
        }
    },
    methods: {
        async camera() {
            const result = await $.camera()
            const recognition = await $.post({
                url: "/aiwith/api/plate/getPlate",
                data: {
                    plate: result
                }
            })
            if (recognition.number != null) {
                this.initData = await $.post({
                    url: "/api/home/getPlate",
                    data: {
                        plate: recognition.number
                    }
                })
                this.initData.licensePlate = recognition.number
            }
            if (this.initData.model == null) {
                this.initData.model = recognition.brand
            }
            this.initData.file = []
            this.initData.file[0] = recognition.image
            if (recognition.number == null) {
                this.reportFields = this.reportFields.filter(field => field.name !== 'licensePlate');
            }

            this.$nextTick(() => {
                this.$refs.action.show();
            });
        },
        async scan() {
            const code = await $.scan()
            $.success(code)
        },
        finish() {
            this.showArea = false
        },
        beforeSubmit(formData) {
            console.log(formData)
            return false
        }
    },
};
</script>
<style lang="scss" scoped>
.quickNavi {
    --van-grid-item-content-background: rgba(255, 255, 255, 0.9);
    margin: -90px 12px 12px 12px;
    backdrop-filter: blur(6px);
    background: rgba(255, 255, 255, 0.9);
    border-radius: 8px;

    :deep(.van-grid-item__content) {
        border-radius: 8px;
        background: none;
    }
}
</style>
