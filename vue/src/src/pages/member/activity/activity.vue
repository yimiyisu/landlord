<template>
    <z-list ref="list" title="我的活动" url="/do/join/act_apply" code="registerState" tabName="status">
        <template #row="item">
            <van-cell-group inset :key="item.id" class="activity-item">
                <van-cell  class="van-multi-ellipsis--l2">
                    <template #title>
                        <span class="custom-title">{{ item.titleLeft }}</span>
                        <van-tag :type="item.status === 1 ? 'primary' : item.status === 2 ? 'danger' : 'default'" class="status-tag">{{ statusMap(item.status) }}</van-tag>
                    </template>
                    <template #label>
                        <div>地址：{{ formattedAddress(item.addressLeft) }} </div>
                        <div>时间：{{ formattedTime(item.beginTimeleft) + '~' +
                            formattedTime(item.endTimeleft) }}</div>
                    </template>
                </van-cell>
                <van-cell>
                    <van-button type="primary" size="small" @click.stop="viewActivity(item)"
                        class="mr-10">查看活动</van-button>
                    <van-button v-if="item.status === 1" type="primary" size="small"
                        @click.stop="cancelApply(item,2)">取消报名</van-button>
                    <van-button v-if="item.status === 2" type="primary" size="small"
                        @click.stop="cancelApply(item,1)">再次报名</van-button>
                </van-cell>
            </van-cell-group>
        </template>
    </z-list>
</template>

<script>
export default {
    data() {
        return {};
    },
    created() {
    },
    methods: {
        viewActivity(item) {
            this.$router.push({ path: `/bbs/detail`, query: { id: item.activityId, type: 'activity' } })
        },
        cancelApply(item,status) {
            $.post({
                url: '/do/patch/act_apply',
                data: {
                    id: item.id,
                    status: status
                }
            }).then(() => {
                this.$refs.list.refresh()
            })
        },
        statusMap(status) {
            switch (status) {
                case 1:
                    return '已报名';
                case 2:
                    return '已取消';
                case 3:
                    return '已结束';
                default:
                    return '';
            }
        },
        formattedAddress(address) {
            return address ? JSON.parse(address).label : '--'
        },
        formattedTime(timestamp) {
            if (timestamp) {
                const date = new Date(timestamp * 1000);
                return date.toLocaleString();
            }
            return ''
        }
    }
};
</script>

<style scoped>
.activity-item {
    margin-top: 10px;
}

.mr-10 {
    margin-right: 10px;
}

.status-tag {
    margin-left: 10px;
}
</style>