<template>
    <z-action label="发布" url="/do/put/community_activity" :fields="fields" type="bubble" position="top"
        :beforeSubmit="submit" v-model:offset="offset" :gap="10" />
</template>
<script>
export default {
    props: {
        type: String
    },
    computed: {
        params() {
            return { type: this.type }
        },
        fields() {
            if (this.type === 'latest') {
                return this.latestFields
            }
            if (this.type === 'activity') {
                return this.activityFields
            }
            if (this.type === 'market') {
                return this.marketFields
            }
            return null
        }
    },
    data() {
        return {
            offset: { x: window.innerWidth - 70, y: window.innerHeight - 120 },
            latestFields: [
                { label: '动态内容', name: 'content', type: 'textarea', maxlength: 500 },
                { label: '选择图片', name: 'image', multiple: true, limit: 9, type: 'image' }],
            activityFields: [
                { label: '活动标题', name: 'title', maxlength: 500 ,required: true},
                { label: '活动地址', name: 'address',type: 'map' },
                { label: '是否收费', name: 'ischarge', type: 'select', options: [{ label: '收费', value: "1" }, { label: '免费', value: "0" }], default: "0" },
                { label: '金额', name: 'amount', type: 'money', visible: (formData) => formData.ischarge == "1" },
                { label: '活动时间', name: 'time', type: 'daterange' },
                { label: '活动内容', name: 'content', maxlength: 500, type: 'textarea' },
                { label: '活动图片', name: 'image', multiple: true, limit: 9, type: 'image' },
            ],
            marketFields: [
                { name: 'marketType', label: '集市类型', type: 'select', code: 'marketType' ,default: 'product'},
                {
                    name: 'other', children: [
                        { label: '标题', name: 'title', maxlength: 500 },
                        { label: '商品描述', name: 'content', type: 'textarea', maxlength: 500 },
                        { label: '选择图片', name: 'image', multiple: true, limit: 9, type: 'image' },
                        { label: '在线收款', name: 'isOnlinepay', type: 'switch', placeholder: '单人付款500元以下' },
                        { label: '原价', name: 'oriPrice', type: 'money' },
                        { label: '现价', name: 'curPrice', type: 'money' }
                    ], visible: (formData) => formData.marketType === 'product'
                },
                {
                    name: 'other', children: [
                        { label: '租赁内容', name: 'content', type: 'textarea', maxlength: 500 },
                        { label: '选择图片', name: 'image', multiple: true, limit: 9, type: 'image' },
                        { label: '租金', name: 'rent', type: 'money' },
                        {
                            label: '押金模式', name: 'depositType', type: 'select', code: 'depositType'
                        }
                    ], visible: (formData) => formData.marketType === 'lease'
                },
                {
                    name: 'other', children: [
                        { label: '标题', name: 'title', maxlength: 500 },
                        { label: '商品描述', name: 'content', type: 'textarea', maxlength: 500 },
                        { label: '选择图片', name: 'image', multiple: true, limit: 9, type: 'image' },
                        { label: '在线收款', name: 'isOnlinepay', type: 'switch', placeholder: '单人付款200元以下' },
                        { label: '单价', name: 'unitPrice', type: 'money' },
                        { label: '每单限购', name: 'limit', type: 'number' },
                        { label: '总库存', name: 'stock', type: 'number' },
                    ], visible: (formData) => formData.marketType === 'dicker'
                },
                {
                    name: 'other', children: [
                        { label: '标题', name: 'title', maxlength: 500 },
                        { label: '商品描述', name: 'content', type: 'textarea', maxlength: 500 },
                        { label: '选择图片', name: 'image', multiple: true, limit: 9, type: 'image' }
                    ], visible: (formData) => formData.marketType === 'present'
                }
            ]
        };
    },
    methods: {
        submit(formData) {
            if (this.type === 'market') {
                formData.other.marketType = formData.marketType
                formData.other.type = 'market'
                return formData.other
            }
            formData.type = this.type
            return formData
        }
    },
}
</script>
