<template>
    <van-nav-bar v-if="visible" :title="title" :fixed="fixed" :z-index="10" left-text="返回" left-arrow
        @click-left="goBack">
        <template #left>
            <slot name="left"></slot>
        </template>
        <template #title>
            <slot name="title"></slot>
        </template>
        <template #right>
            <slot name="right"></slot>
        </template>
    </van-nav-bar>
</template>

<script>
export default {
    name: "topBar",
    props: {
        // 标题
        title: {
            type: String,
            default: ""
        },
        // 是否固定
        fixed: {
            type: Boolean,
            default: true
        },
        // 返回事件
        goBack: {
            type: Function,
            default: () => {
                window.history.go(-1);
            }
        }
    }
};
</script>
