<template>
    <!-- 车辆列表内容 -->
    <z-list v-if="this.houseId" title="我的车辆" url="/api/personal/myParkingInfo" :params="house">
        <template #row="item">
            <van-cell-group inset>
                <van-cell title="车牌号" :value="item.title" />
                <van-cell title="品牌类型" :value="item.model" />
                <van-cell title="登记时间" :value="formattedTime(item.createGmt)" />
                <van-cell title="状态">
                    <template #value>
                        <z-dict code="carStatus" :modelValue="item.status" />
                    </template>
                </van-cell>
                <van-cell v-if="item.parkingId" title="我的车位">
                    <template #value>
                        <z-text depend="parking" :modelValue="item.parkingId" />
                    </template>
                    <template #label>
                        <span style="margin-right: 10px;">区域:{{ item.areaTitle }}</span>
                        类型:<z-dict code="cfg_parkingType" :modelValue="item.parkingType" />
                    </template>
                </van-cell>
                <van-cell v-if="item.endTime" title="到期时间" :value="formattedTime(item.endTime)" />
                <z-action v-if="item.status === 1" label="租赁缴费" position="top" size="small" style="margin: 10px 15px">
                    <z-block url="/api/fee/payConfig" :params="{ id: item.id }">
                        <template #default="data">
                            <z-form :fields="payFields" url="/api/fee/rentParking" :data="{ id: item.id }">
                                <template #money="formData">
                                    <z-money :modelValue="data.price * formData.num" />
                                </template>
                            </z-form>
                        </template>
                    </z-block>
                </z-action>
            </van-cell-group>
        </template>
    </z-list>
    <van-empty image="error" description="暂无车位" v-else/>

    <!-- 车辆登记 -->
    <z-action type="bubble" label="登记" position="top" url="/do/put/car" :fields="fields" :beforeSubmit="submit" />
</template>
<script>
export default {
    inject: ['houseId'],
    data() {
        return {
            fields: [
                { name: 'title', label: '车牌号' },
                { name: 'model', label: '品牌型号' }
            ],
            payFields: [
                { name: 'num', label: '租赁时长（月）', type: 'number' },
                { name: 'money', label: '交付金额' }
            ],
            params: null,
        }
    },
    computed: {
        house() {
            return { houseId: this.houseId }
        }
    },
    methods: {
        formattedTime(timestamp) {
            if (timestamp) {
                const date = new Date(timestamp * 1000);
                return date.toLocaleString(); // 可以根据需要调整格式
            }
        },
        submit(data) {
            data.houseId = this.houseId
            return data
        }
    }
}
</script>
