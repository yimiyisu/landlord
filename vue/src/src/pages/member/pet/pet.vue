<template>
    <div style="padding: 10px; box-sizing: border-box;">
        <!-- 宠物列表 -->
        <z-list v-if="this.houseId" title="我的宠物" url="/do/select/pet" :params="house" emptyText="暂无宠物">
            <template #row="item">
                <van-cell-group inset class="pet-card">
                    <van-cell title="宠物名字" :value="item.title" />
                    <van-cell title="宠物类型" :value="item.species">
                        <z-dict :modelValue="item.species" code="petType" />
                    </van-cell>
                    <van-cell title="照片" v-if="item.image">
                        <z-image v-model="item.image" />
                    </van-cell>
                    <van-cell title="登记日期">
                        <z-date :value="item.createGmt" />
                    </van-cell>
                    <van-cell v-if="item.note" title="备注" :value="item.note" />
                    <van-button type="primary" size="small" style="margin: 10px;"
                        @click="showPetLog(item)">接种记录</van-button>
                </van-cell-group>
            </template>
            <!-- 上传宠物 -->
            <z-action label="登记" type="bubble" position="top" url="/do/put/pet" :fields="fields"
                :beforeSubmit="submit" />
        </z-list>
    </div>
</template>

<script>
export default {
    inject: ['houseId'],
    data() {
        return {
            fields: [
                { name: 'title', label: '名字' },
                { name: 'image', label: '图片', type: 'image', multiple: true },
                { name: 'species', label: '品种', code: 'petType' },
                { name: 'note', label: '备注', type: 'textarea' }
            ],
            logFields: [
                { name: 'title', label: '疫苗名称' },
                { name: 'inoculationTime', label: '接种时间', type: 'date' },
                { name: 'nextTime', label: '下次接种时间', type: 'date' },
                { name: 'prove', label: '接种证明', type: 'image', multiple: true }
            ]
        };
    },
    computed: {
        house() {
            return { houseId: this.houseId }
        },
        getPetLogParams() {
            return (pet) => {
                return { petId: pet.id };
            };
        }
    },
    created() {
    },
    methods: {
        submit(data) {
            data.houseId = this.houseId
            return data
        },
        showPetLog(pet) {
            this.$router.push({
                path: '/member/petLog',
                query: {
                    petId: pet.id
                }
            })
        }
    }
};
</script>
<style scoped>
.pet-card {
    margin-bottom: 20px;
    border-radius: 8px;
    /* 可选：添加圆角 */
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    /* 可选：添加阴影 */
}
</style>
