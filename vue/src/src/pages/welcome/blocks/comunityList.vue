<template>
    <van-popup v-model:show="show" position="top" :style="{ height: '100vh', width: '100vw' }">
        <van-nav-bar title="请先选择小区" />
        <z-form :fields="fields" :beforeSubmit="confirm" />
    </van-popup>
</template>
<script>
import Picker from './picker.vue'
export default {
    data() {
        return {
            areaId: null,
            show: true,
            activeIndex: 0,
            fields: [
                { name: 'area', label: '选择地区', type: 'address' },
                { name: 'community', label: '选择小区', component: Picker }
            ]
        }
    },
    methods: {
        confirm(formData) {
            if (!formData.community) {
                return $.error('请选择小区')
            }
            $.tenant(formData.community)
            this.show = false
            this.$emit('finish')
        }
    },
}
</script>
