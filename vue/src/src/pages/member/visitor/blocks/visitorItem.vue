<template>
    <van-cell-group inset style="margin-bottom: 15px;">
        <van-cell title="姓名" :value="item.title" />
        <van-cell title="联系电话" :value="item.telephone" />
        <van-cell v-if="item.licensePlate" title="车牌号" :value="item.licensePlate" />
        <van-cell title="创建时间">
            <z-date :value="item.createGmt" />
        </van-cell>
        <div class="action" style="display: flex; justify-content: flex-end;">
            <z-action size="small" url="/api/visitor/put" :data="item" style="margin: 10px" label="访客预约"
                :fields="visitorFields" position="top" :beforeSubmit="beforeSubmit" />
        </div>
    </van-cell-group>
</template>

<script>
export default {
    props: {
        item: Object,
    },
    data() {
        return {
            fields: [
                { name: 'title', label: '姓名' },
                { name: 'telephone', label: '联系电话' },
                { name: 'licensePlate', label: '车牌号' },
                { name: 'createGmt', label: '创建时间', type: 'date' },
            ],
            visitorFields: [
                { name: 'licensePlate', label: '车牌号' },
                { name: 'time', label: '预约时间', type: 'date' },
                {
                    name: 'timeRange', label: '时间范围', type: 'select', options: [
                        { label: '上午', value: '1' },
                        { label: '下午', value: '2' },
                        { label: '晚上', value: '3' }
                    ]
                }
            ]
        }
    },
    methods: {
        beforeSubmit(data) {
            return {
                ...data,
                id: null,
                realname:data.title,
            }
        }
    },
}
</script>
