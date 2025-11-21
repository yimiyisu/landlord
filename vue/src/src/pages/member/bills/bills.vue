<template>
    <van-tabs v-model:active="activeName">
        <van-tab title="水费" name="water"/>
        <van-tab title="电费" name="electric"/>
    </van-tabs>
    <WaterList v-if="activeName === 'water'"/>
    <ElectricList v-else/>
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
            activeName: 'water',
        };
    },
};
</script>
