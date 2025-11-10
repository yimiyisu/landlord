<template>
    <div>
        <van-nav-bar title="业主身份绑定" />
        <z-form url="/api/member/apply" :fields="fields" @finish="finish" />
    </div>
</template>

<script>
import HouseSelect from './blocks/houseSelect.vue';
export default {
    data() {
        return {
            fields: [
                { label: '姓名', name: 'title' },
                { label: '联系电话', name: 'telephone' },
                { label: '身份证', name: 'idcard' },
                { label: '房产', name: 'houseId', component: HouseSelect },
                { label: '人员类型', name: 'type', code: 'landorType' }
            ]
        };
    },
    methods: {
        finish() {
            this.$router.push({ path: '/member/index' })
        }
    }
};
</script>
