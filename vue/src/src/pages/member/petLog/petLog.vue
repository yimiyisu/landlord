<template>
    <z-list v-if="show" title="接种列表" url="/do/select/pet_log" :params="params" :emptyText="暂无接种记录">
        <template #row="logItem">
            <van-cell-group inset class="pet-log-card">
                <van-cell title="疫苗名称" :value="logItem.title" />
                <van-cell title="接种时间" :value="logItem.inoculationTime" >
                    <z-date :value="logItem.inoculationTime"/>
                </van-cell>
                <van-cell title="下次接种时间" :value="logItem.nextTime" >
                    <z-date :value="logItem.nextTime"/>
                </van-cell>
                <van-cell title="接种凭证">
                    <z-image v-if="logItem.prove" :modelValue="logItem.prove" :multiple="true" />
                </van-cell>
            </van-cell-group>
        </template>
        <z-action label="记录" position="top" size="small" :fields="logFields" url="/do/put/pet_log"
            :data="params" type="bubble"/>
    </z-list>
</template>

<script>
export default {
    data() {
        return {
            logFields: [
                { name: 'title', label: '疫苗名称' },
                { name: 'inoculationTime', label: '接种时间', type: 'date' },
                { name: 'nextTime', label: '下次接种时间', type: 'date' },
                { name: 'prove', label: '接种证明', type: 'image', multiple: true }
            ],
            show:false,
        };
    },
    created() {
        this.params = {
            petId: this.$route.query.petId
        }
        this.show = true
    },
};
</script>
<style scoped>
.pet-log-card {
    margin-bottom: 10px;
}
</style>
