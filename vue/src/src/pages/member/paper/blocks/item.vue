<template>
    <div class="value-card" :class="value.status">
        <div class="main-content">
            <van-cell class="ticket-cell" title="" :border="false">
                <template #title>
                    <div class="describe-text">
                        {{ value.describe }}
                    </div>
                </template>
                <template #label>
                    <div class="image-container">
                        <z-image v-if="value.images && value.images.length > 0" :modelValue="value.images.slice(0, 3)"
                            readonly="true" />
                    </div>
                </template>
            </van-cell>
            <z-dict class="status" v-model="value.status" code="workorderStatus" />
        </div>
        <div class="value-time">{{ formatTime(value.createGmt) }}</div>
    </div>
</template>

<script>
export default {
    props: {
        value: Object
    },
    methods: {
        formatTime(timestamp) {
            if (timestamp) {
                const date = new Date(timestamp * 1000);
                return date.toLocaleString();
            }
            return '';
        },
    }
}
</script>

<style scoped>
.value-card {
    margin-bottom: 8px;
    border-bottom: 1px solid #eee;
    cursor: pointer;
}

.main-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    /* 垂直居中 */
}

.ticket-cell {
    /* 可以根据需要调整卡片样式 */
    margin-bottom: 10px;
    flex: 1;
    /* 让 van-cell 占据剩余空间 */
}

.describe-text {
    font-size: 14px;
    color: #333;
    word-break: break-word;
    margin-top: -5px;
}

.image-container {
    margin-top: 12px;
}

.status {
    font-size: 13px;
    font-weight: 500;
    min-width: 60px;
    text-align: right;
    margin-left: 15px;
}

.value-time {
    font-size: 12px;
    color: #999;
    text-align: left;
    padding-top: 2px;
    padding-bottom: 3px;
}
</style>
