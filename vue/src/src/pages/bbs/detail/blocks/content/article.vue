<template>
  <CreatorInfo :detailInfo="detailInfo" />

  <div class="main">
    <div class="title">{{ detailInfo.title }}</div>
    <Activity v-if="detailInfo.type === 'activity'" :detailInfo="detailInfo" />
    <div class="content">{{ detailInfo.content }}</div>
    <div class="imglist">
      <z-image v-if="detailInfo.image" :modelValue="detailInfo.image" />
    </div>
    <Shell />
  </div>
</template>
<script>
import CreatorInfo from './creatorInfo.vue';
export default {
  components: {
    CreatorInfo
  },
  props: {
    detailInfo: Object
  }
}
</script>
<style scoped>
.main {
  .title {
    font-size: 16px;
    line-height: 20px;
    font-weight: bold;
    color: #333;
    margin-top: 10px;
  }

  .content {
    font-size: 14px;
    color: #333;
    line-height: 20px;
    margin-top: 10px;
    margin-bottom: 10px;
  }

  .imglist {
    overflow: hidden;
    border-radius: 10px;
  }

  .img {
    display: block;
    margin-bottom: 2px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
:deep(.z-image .van-image) {
    width: 100%;
    height: 100%;
    object-fit: cover;
    aspect-ratio: 1 / 1;
}
</style>