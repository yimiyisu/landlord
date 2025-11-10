<template>
    <div>
        <van-field v-model="fieldValue" input-align="right" is-link readonly label="房产" placeholder="请选择房产"
            @click="show" />
        <van-popup v-model:show="showHouse" round position="bottom">
            <van-cascader v-model="cascaderValue" title="请选择房产" :options="houseList" @close="showHouse = false"
                @finish="onFinish" :field-names="fieldNames" />
        </van-popup>
    </div>
</template>

<script>
export default {
    data() {
        return {
            houseList: [],
            showHouse: false,
            fieldNames: { text: 'title', value: 'id', children: 'children' },
            fieldValue: '',
            cascaderValue: '',
        }
    },

    methods: {
        async show() {
            this.houseList = await $.get({
                url: "/api/member/getHouseList"
            })
            this.showHouse = true
        },
        onFinish({ selectedOptions }) {
            const lastOption = selectedOptions[selectedOptions.length - 1];
            this.fieldValue = selectedOptions.map(opt => opt.title).join(' ')
            this.$emit('update:modelValue', lastOption.id);
            this.showHouse = false;
        },
    }
}
</script>

<style></style>
