<template>
    <van-cell-group inset style="margin-bottom: 15px;">
        <van-cell title="访客姓名" :value="item.realname" />
        <van-cell title="联系电话" :value="item.telephone" />
        <van-cell v-if="item.licensePlate" title="车牌号" :value="item.licensePlate" />
        <van-cell title="预约日期">
            <z-date :value="item.time" />
        </van-cell>
        <van-cell v-if="item.reason" title="来访理由" :value="item.reason" />
        <van-cell title="来访状态" :value="item.isEnter === 1 ? '已到访' : '未到访'" />
        <van-cell v-if="item.isEnter==1" title="来访时间">
            <z-date :value="item.updateGmt" />
        </van-cell>
        <div class="action" style="display: flex; justify-content: flex-end;">
            <Action :item="item" :fields="fields" />
        </div>
    </van-cell-group>
</template>

<script>
import Action from './action.vue';
export default {
    components:{Action},
    props: {
        item: Object,
        fields: Array
    },
}
</script>
