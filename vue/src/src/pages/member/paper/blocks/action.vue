<template>
    <z-action v-if="paperInfo.status == 0 || paperInfo.status == 1" label="修改工单" size="small"
        url="/do/patch/workorderByUid" :data="paperInfo" :fields="fields" style="margin-left: 5px;" />
    <z-action v-if="paperInfo.status == 0" label="取消工单" size="small" url="/do/patch/workorder_cancel"
        :data="{ id: paperInfo.id }" mode="confirm" type="danger" style="margin-left: 5px;" />
</template>
<script>
export default {
    props: {
        paperInfo: Object
    },
    data() {
        return {
            fields: [
                { name: 'describe', label: '描述', type: 'textarea' },
                {
                    name: 'images', label: '图片', type: 'image', multiple: true
                },
                { name: 'phone', label: '联系电话', type: 'input' },
                { name: 'file', label: '附件', type: 'attach', multiple: true },
                { name: 'priority', label: '加急', type: 'switch' },
            ],
        }
    },

}
</script>
