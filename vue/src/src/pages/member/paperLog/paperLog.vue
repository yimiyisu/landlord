<template>
    <z-block url="/do/get/workorder" :params="params">
        <template #default="paperInfo">
            <WorkorderDetail :paperInfo="paperInfo" />
            <z-block v-if="paperInfo.id" url="/do/list/work_log" :params="{ workorderId: paperInfo.id }">
                <template #default="paperLogList">
                    <div style="margin:0 15px; padding: 0 8px;">
                        <div class="loglist-title">处理记录：</div>
                        <LogList v-if="paperLogList" :paperLogList="paperLogList" />
                        <van-empty v-else>
                            暂无处理记录
                        </van-empty>
                    </div>
                </template>
            </z-block>
        </template>
    </z-block>
</template>

<script>
import LogList from './blocks/logList.vue';
import WorkorderDetail from './blocks/workorderDetail.vue';
export default {
    components: { LogList, WorkorderDetail },
    data() {
        return {
            params: {},
        };
    },
    async created() {
        const { id } = this.$route.query;
        this.params = { id: id };
    },
};
</script>
<style scoped>
.main {
    padding: 15px;
}

.loglist-title {
    font-size: 16px;
    font-weight: bold;
    margin: 10px 0;
}

.action {
    display: flex;
    /* 使用 flex 布局 */
    gap: 10px;
    /* 按钮之间的间距 */
    margin-top: 15px;
    /* 按钮与上方内容的间距 */
}
</style>
