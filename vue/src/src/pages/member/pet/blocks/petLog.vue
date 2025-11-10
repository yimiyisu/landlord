<template>
  <van-cell-group inset class="pet-log-card">
    <van-cell title="疫苗名称" :value="data.title" />
    <van-cell title="接种时间" :value="data.inoculationTime" />
    <van-cell title="下次接种时间" :value="data.nextTime" />
    <van-cell title="接种凭证">
      <z-image :modelValue="data.prove" :multiple="true" />
    </van-cell>
  </van-cell-group>
</template>
<script>
export default {
  props: {
    data: Object
  }
}
</script>

<style scoped>
.pet-log-card {
    margin-bottom: 20px;
    border-radius: 8px;
    /* 可选：添加圆角 */
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    /* 可选：添加阴影 */
}
</style>
