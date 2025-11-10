<template>
    <div class="shell">
        <z-action :fields="fields" url="/api/community/tipping" position="top" :data="data" @finish="refresh">
            <template #label>
                <van-icon name="point-gift-o" />
            </template>
        </z-action>
        <div v-for="item in shells" :key="item.id">
            <z-user :value="item.uid" />:
            <z-dict code="tippingMessage" :modelValue="item.message" />
            {{ item.shell > 0 ? '赠送贝壳:' : '扣除贝壳:' }}{{ Math.abs(item.shell) }}
        </div>
    </div>
</template>
<script>
export default {
    props: {
        shells: Array,
        commentId: String
    },
    computed: {
        data() {
            return { commentId: this.commentId }
        }
    },
    data() {
        return {
            fields: [
                {
                    label: '操作', name: 'type', type: 'radiobox', options: [
                        { label: '赠送', value: 1 },
                        { label: '扣除', value: 0 }
                    ], rules: [{ required: true, message: '请选择操作' }]
                },
                { label: '贝壳数', name: 'shell', type: 'number', min: 1 },
                { label: '留言', name: 'message', type: 'select', code: 'tippingMessage' }
            ]
        }
    },
    methods: {
        refresh() {
            this.$emit('refresh')
        }
    }
}
</script>
<style scoped lang="scss">
.shell {
    background-color: #f1f2f3;
    padding: 8px;
}
</style>
