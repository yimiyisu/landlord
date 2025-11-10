<template>
    <van-field :modelValue="title" label="您的小区" input-align="right" is-link @click="click" />
    <van-popup v-model:show="visible" destroy-on-close round position="bottom">
        <van-picker title="选择小区" :columns="list" @confirm="confirm" @cancel="cancel" />
    </van-popup>
</template>
<script>
export default {
    inheritAttrs: false,
    props: {
        formData: Object
    },
    data() {
        return { title: null, visible: false, list: null }
    },
    methods: {
        confirm({ selectedOptions, selectedValues }) {
            if (selectedValues.length < 1) {
                return $.error('请选择小区')
            }
            this.title = selectedOptions[0].text
            this.$emit('update:modelValue', selectedValues[0])
            this.cancel()
        },
        cancel() {
            this.visible = false
        },
        async click() {
            const { formData } = this
            if (!formData.area || formData.area.length !== 3) {
                return $.error('请先选择地域')
            }
            const lastId = formData.area[2].value
            const result = await $.get({ url: '/do/list/communityByArea', data: { lastId } })
            if (!result || result.length === 0) {
                return $.error('该地域没有开通的小区')
            }
            this.list = result.map((item) => {
                return { text: item.title, value: item.id }
            })
            this.visible = true
        }
    },
}
</script>
