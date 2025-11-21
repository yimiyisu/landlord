<template>
    <!-- 访客列表 -->
    <z-list title="访客预约" url="/do/select/visitor_type1" :size="20" emptyText="暂无预约列表">
        <template #row="item">
            <div class="order-item">
                <Item :item="item" :fields="fields" />
            </div>
        </template>
        <z-action v-if="actionShow" label="预约" url="/api/visitor/put" :fields="fields" position="top" type="bubble" :beforeSubmit="beforeSubmit" />
    </z-list>
</template>

<script>
import Item from './item.vue';
export default {
    components: { Item },
    props: {
        actionShow: Boolean
    },
    data() {
        return {
            fields: [
                { name: 'type', label: '访客类型', type: 'text', default: 1, code: 'visitorType'},
                { name: 'realname', label: '姓名' },
                { name: 'telephone', label: '联系电话' },
                { name: 'licensePlate', label: '车牌号' },
                { name: 'time', label: '预约时间', type: 'date',required: true },
                {
                    name: 'timeRange', label: '时间范围', type: 'select', options: [
                        { label: '上午', value: '1' },
                        { label: '下午', value: '2' },
                        { label: '晚上', value: '3' }
                    ],required: true
                },
                { name: 'reason', label: '来访理由', type: 'textarea' }
            ],
        };
    },
    methods: {
        beforeSubmit(formData) {
           if (!formData.time || !formData.timeRange) {
                $.error('请选择预约时间和时间范围')
                return false
           }
           return formData
        }
    },
}
</script>

<style scoped lang="scss">
.order-item {
    background: #fff;
    margin: 10px 15px 0;
    padding: 0;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
</style>
