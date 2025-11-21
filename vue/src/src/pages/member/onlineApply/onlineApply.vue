<template>
    <z-list title="我的申请" url="/do/select/online_apply" code="cfg_applyType" tabName="type" :size="10" emptyText="暂无申请">
        <template #row="item">
            <van-cell-group inset style="margin-top: 10px;">
                <van-cell title="申请时间" :value="formattedTime(item.createGmt)" />
                <van-cell title="申请类型">
                    <template #value>
                        <z-dict code="cfg_applyType" :modelValue="item.type" />
                    </template>
                </van-cell>
                <van-cell title="联系电话">
                    <template #value>
                        <span>{{ item.phone }}</span>
                    </template>
                </van-cell>
                <van-cell title="申请状态">
                    <template #value>
                        <z-dict code="onlineApplyStatus" :modelValue="item.status" />
                    </template>
                </van-cell>
                <van-cell title="驳回理由" v-if="item.status === 3">
                    <template #value>
                        <span>{{ item.reason }}</span>
                    </template>
                </van-cell>
                <div class="action">
                    <z-action label="编辑" size="small" :data="item" :fields="fields" url="/do/patch/online_apply" />
                </div>
            </van-cell-group>
        </template>
        <z-action type="bubble" label="申请" position="top" url="/do/put/online_apply" :fields="fields" />
    </z-list>
</template>

<script>
import Extra from './blocks/extra.vue';
export default {
    data() {
        return {
            fields: [
                { name: 'type', label: '类型', code: 'applyType' },
                { name: 'phone', label: '联系方式' },
                { name: 'extra', label: '补充信息', component: Extra },
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
<style>
.action {
        display: flex;
        margin: 10px 15px;
    }
</style>
