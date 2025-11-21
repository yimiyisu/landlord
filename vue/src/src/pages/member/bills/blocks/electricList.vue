<template>
    <div style="margin-top: 15px">
        <z-list url="/do/select/electric_log" :params="house" emptyText="暂无账单">
            <template #row="item">
                <van-cell-group inset class="card">
                    <van-cell title="创建时间" :value="formattedTime(item.createGmt)" />
                    <van-cell title="应缴时间" :value="formattedTime(item.dueTime)" />
                    <van-cell title="类型">
                        <template #value>
                            <z-dict code="cfg_waterType" :modelValue="item.type" />
                        </template>
                    </van-cell>
                    <van-cell title="本期用量" :value="item.number" />
                    <van-cell title="金额">
                        <template #value>
                            <z-money :modelValue="item.cash" />
                        </template>
                    </van-cell>
                    <van-cell title="缴费状态">
                        <template #value>
                            <z-dict code="payStatus" :modelValue="item.status" />
                        </template>
                    </van-cell>
                    <van-cell v-if="item.status === 2" title="支付时间" :value="formattedTime(item.payTime)" />
                    <van-button v-if="item.status === 1" type="primary" size="small" style="margin: 10px 15px"
                        @click="payment(item.id)">立即支付</van-button>
                </van-cell-group>
            </template>
        </z-list>
    </div>
</template>
<script>
export default {
    props: {
        type: String
    },
    inject: ['houseId'],
    computed: {
        house() {
            return { houseId: this.houseId }
        },
        url() {
            return this.type === 'water' ? '/do/select/water_log' : '/do/select/electric_log'
        }
    },
    methods: {
        formattedTime(timestamp) {
            if (timestamp) {
                const date = new Date(timestamp * 1000);
                return date.toLocaleString(); // 可以根据需要调整格式
            }
        },
        payment(id) {
            console.log('------')
            wx.miniProgram.navigateTo({ url: '/pages/pay/pay?billId=' + id })
        }
    }
}
</script>
<style>
.card {
    margin-bottom: 20px;
    border-radius: 8px;
    /* 可选：添加圆角 */
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    /* 可选：添加阴影 */
}
</style>
