<template>
    <van-tabs v-model:active="active">
        <van-tab title="水费" name="water">
            <WaterList/>
        </van-tab>
        <van-tab title="电费" name="electric">
            <ElectricList/>
        </van-tab>
    </van-tabs>
</template>

<script>
import ElectricList from './blocks/electricList.vue';
import WaterList from './blocks/waterList.vue';
export default {
    inheritAttrs: false,
    components: {
        WaterList,
        ElectricList
    },
    data() {
        return {
            active: 'water',
        };
    },
};
</script>
