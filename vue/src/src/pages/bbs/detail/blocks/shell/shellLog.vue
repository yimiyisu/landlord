<template>
  <van-nav-bar v-if="detailInfo.tippingList" title="文章打赏记录(最近5条)" />
    <ul v-if="detailInfo.tippingList">
      <li v-for="item in detailInfo.tippingList" :key="item.id" style="margin-top: 10px;">
        <div class="content flex-aic">
          <z-avatar :value="item.uid" :plain="false" />:
          <z-dict code="tippingMessage" :modelValue="item?.message" />
          {{ item.shell > 0 ? '赠送贝壳:' : '扣除贝壳:' }}{{ Math.abs(item.shell) }}
        </div>
      </li>
    </ul>
</template>
<script>
export default {
  props: {
    detailInfo: Object
  }
}
</script>
<style scoped>
:deep(.van-image--round) {
  height: 100%;
}
:deep(.z-avatar){
  margin-left: 0;
}
.content {
  font-size: 14px;
  color: #333;
  line-height: 20px;
  margin-top: 10px;
  margin-bottom: 10px;
  gap: 4px;
}
</style>