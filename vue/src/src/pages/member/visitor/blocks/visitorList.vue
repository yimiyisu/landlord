<template>
    <!-- 访客列表 -->
    <z-list title="访客列表" url="/do/select/visitor_cont" :size="20" :emptyText="暂无访客列表">
        <template #row="item">
            <div class="order-item">
                <VisitorItem :item="item" />
            </div>
        </template>
    </z-list>
</template>

<script>
import VisitorItem from './visitorItem.vue';
export default {
    components: { VisitorItem },
    data() {
        return {
        };
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
