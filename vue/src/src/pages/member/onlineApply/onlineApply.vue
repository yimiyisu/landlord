<template>
    <z-list title="我的申请" url="/do/select/online_apply">
        <template #default="item">
            <van-cell-group inset>
                <van-cell title="申请时间" :value="formattedTime(item.createGmt)" />
                <van-cell title="申请类型">
                    <template #value>
                        <z-dict code="cfg_applyType" :modelValue="item.type" />
                    </template>
                </van-cell>
                <van-cell title="申请状态" :value="item.status">
                    <template v-if="item.status === 3" #label>
                        <span>{{ item.reason }}</span>
                    </template>
                </van-cell>
            </van-cell-group>
        </template>
    </z-list>
    <z-action bubbled label="申请" position="top" url="/do/put/online_apply" :fields="fields" />
</template>

<script>
import Extra from './blocks/extra.vue';
export default {
    data() {
        return {
            fields: [
                { name: 'phone', label: '联系方式' },
                { name: 'type', label: '类型', code: 'applyType' },
                { name: 'extra', component: Extra }
            ],
        };
    },
    created() { },
    methods: {
        formattedTime(timestamp) {
            if (timestamp) {
                const date = new Date(timestamp * 1000);
                return date.toLocaleString(); // 可以根据需要调整格式
            }
        },
    }
};
</script>