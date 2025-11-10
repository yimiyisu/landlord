<template>
  <div class="flex-jasc user">
    <div class="flex-aic">
      <z-avatar :value="detailInfo.creator" size="30px" />
      <div style="padding-left: 10px">
        <z-user class="nickname" :value="detailInfo.creator" />
        <div class="desc">发布于：<z-date :value="detailInfo.createGmt" plain /></div>
      </div>
    </div>
    <van-button v-if="isConcerned" type="primary" size="small" round icon="success" plain
      @click="concern(detailInfo.creator)">已关注</van-button>
    <van-button v-else type="primary" size="small" round icon="plus" plain
      @click="concern(detailInfo.creator)">关注</van-button>
  </div>
</template>
<script>
export default {
  props: {
    detailInfo: Object
  },
  data() {
    return {
      isConcerned: false
    }
  },
  async created() {
    const res = await $.get({ url: '/do/count/concern' })
    this.isConcerned = res > 0 ? true : false
  },
  methods: {
    concern(creator) {
      this.isConcerned = this.isConcerned ? false : true
      $.post({ url: '/api/community/concerned', data: { concerned: creator, action: this.isConcerned } })
    }
  },
}
</script>
<style scoped>
:deep(.van-image--round){
    height: 100%;
}
.user {
  .avatar {
    margin-right: 10px;
  }

  .nickname {
    font-size: 16px;
    color: rgba(91, 106, 145);
    font-weight: bold;
    padding: 3px 0;
  }

  .desc {
    color: #999;
    font-size: 12px;
  }
}
</style>