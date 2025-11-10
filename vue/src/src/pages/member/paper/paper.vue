<template>
    <z-list title="我的工单" url="/do/select/workorderMy" code="workorderType" tabName="type" :size="10" ref="list"
        emptyText="暂无工单">
        <template #row="item">
            <div class="order-item">
                <div class="flex-jasc">
                    <div style="color: #333; font-size: 14px;">工单号：{{ item.id }}</div>
                    <z-dict class="type" v-model="item.type" code="workorderType" />
                </div>
                <!-- 工单列表 -->
                <div class="items" @click="handleOrderClick(item.id)">
                    <Item :value="item" />
                </div>
                <div class="action">
                    <action :paperInfo="item" />
                </div>
            </div>
        </template>
        <z-action label="新建" url="/do/put/workorder" :fields="fields" type="bubble" position="top" />
    </z-list>
</template>

<script>
import Action from './blocks/action.vue';
import Item from './blocks/item.vue';
export default {
    components: { Item, Action },
    data() {
        return {
            fields: [
                { name: 'type', label: '工单类型', code: 'workorderType', default: 0 },
                { name: 'licensePlate', label: '车牌号', visible: (formData) => formData.type && formData.type > 1, },
                { name: 'houseId', label: '房屋', type:'house',visible:(formData)=>formData.type == 1 ||formData.type == 0 },
                { name: 'describe', label: '描述', type: 'textarea' },
                { name: 'images', label: '图片', type: 'image', multiple: true },
                { name: 'phone', label: '联系电话', type: 'input' },
                { name: 'files', label: '附件', type: 'attach', multiple: true },
                { name: 'priority', label: '加急', type: 'switch' },
            ],
        };
    },
    computed: {
        params() {
            const { type } = this
            return type ? { type } : null
        },
    },
    methods: {
        handleOrderClick(orderId) {
            this.$router.push({
                path: '/member/paperLog',
                query: { id: orderId }
            });
        },
    },
};
</script>
<style>
.order-list {
    background: #f5f5f5;
}

.flex-jasc {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.order-item {
    background: #fff;
    margin: 10px 15px 0;
    padding: 12px 15px;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);

    .status {
        font-size: 14px;
        color: #FF4827;
    }

    .items {
        margin: 8px 0;
        border-top: 1px solid #f5f5f5;
        padding-top: 12px;
    }
    .action {
        display: flex;
        justify-content: flex-end;
        margin: 5px;
    }
}
</style>
