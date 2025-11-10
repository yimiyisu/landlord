<template>
    <div>
        <van-grid :column-num="5">
            <van-grid-item :class="{activetab: type == item.type}" @click="changeTab(item)" v-for="item,index in tabs" :key="index" :icon="item.icon" :icon-color="item.iconColor" :text="item.label" />
        </van-grid>

        <div class="main">
            <div v-if="type == 'service'">
                <serviceItem v-for="_, index in 3" :key="index"></serviceItem>
            </div>
            <div v-else>
                <Item v-for="_, index in 3" :key="index" :type="type" :data="item"></Item>
            </div>
        </div>
    </div>
</template>

<script>
import Item from '../blocks/item.vue'
import serviceItem from '../blocks/serviceItem.vue';
export default {
    components: {
        Item,
        serviceItem
    },
    data() {
        return {
            type: 'like',
            tabs: [
                {label: '获赞', type: 'like', icon: 'like', iconColor: '#FB5130'},
                {label: '收藏', type: 'collect', icon: 'star', iconColor: '#F7C451'},
                {label: '服务', type: 'service', icon: 'service', iconColor: '#7115A7'},
                {label: '关注', type: 'follow', icon: 'contact', iconColor: '#4E84F2'},
                {label: '评论', type: 'comment', icon: 'comment-circle', iconColor: '#81DC8D'},
            ]
        };
    },
    created() { },
    methods: {
        changeTab(item) {
            // 切换消息类型
            this.type = item.type
        }
    }
};
</script>
<style scoped lang="scss">
.main{padding: 10px 0; box-sizing: border-box;}
.label{padding-left: 10px; font-size: 14px; color: #333;}
:deep(.van-cell){padding: 20px 16px;}
// .activetab{
//     :deep(.van-grid-item__content){background-color: #eee;}
// }
</style>
