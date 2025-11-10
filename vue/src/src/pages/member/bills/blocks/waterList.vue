<template>
    <div style="margin-top: 15px">
        <z-list v-if="this.houseId" url="/do/select/water_log" :params="house" emptyText="暂无账单">
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
    },
    methods: {
        formattedTime(timestamp) {
            if (timestamp) {
                const date = new Date(timestamp * 1000);
                return date.toLocaleString(); // 可以根据需要调整格式
            }
        },
        onBridgeReady(payParams) {
            const { WeixinJSBridge } = window
            WeixinJSBridge.invoke(
                'getBrandWCPayRequest',
                {
                    "appId": payParams.appId,
                    "timeStamp": payParams.timeStamp,
                    "nonceStr": payParams.nonceStr,
                    "package": payParams.package,
                    "signType": payParams.signType,
                    "paySign": payParams.paySign
                },
                function (res) {
                    if (res.err_msg === "get_brand_wcpay_request:ok") {
                        alert('支付成功')
                        // $.post({ // 根据商户订单号主动查询支付状态
                        //   url: "/api/home/queryStatus",
                        //   data: { outTradeNo: payParams.outTradeNo }
                        // })
                    } else {
                        alert('支付失败：' + res.err_msg)
                    }
                })
        },

        async payment(id) {
            const payId = await $.post({ url: '/api/fee/hydropowerPay', data: { id: id, type: "water", action: "pay" } })
            console.log(payId)
            const params = await $.post({ url: '/pay/api/createOrder/toPay', data: { payId: payId } })
            this.onBridgeReady(params)
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
