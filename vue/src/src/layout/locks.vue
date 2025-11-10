<template>
    <van-popup v-bind="$attrs" position="top" closeable :style="{ height: '50%' }">
        <div class="wrap">
            <h3>常用门禁</h3>
            <van-grid>
                <van-grid-item text="东门门禁" />
                <van-grid-item text="南区门禁" />
                <van-grid-item text="1幢楼门禁" />
            </van-grid>
            <h3>其它公共门禁</h3>
            <van-grid>
                <van-grid-item text="东门门禁" />
                <van-grid-item text="北门门禁" />
                <van-grid-item text="南门门禁" />
                <van-grid-item text="西门门禁" />
                <van-grid-item text="梧桐郡门禁" />
            </van-grid>
        </div>
    </van-popup>
</template>
<script>
export default {}
</script>
<style scoped>
.wrap {
    margin: 0 20px;
}
</style>
