<template>
  <div>
    <!-- 未关注   已关注  互关 -->
    <van-button v-if="type == 'follow'" type="primary" size="small" round plain icon="plus">关注</van-button>
    <van-button v-if="type == 'comment'" type="default" size="small" round plain
      @click.stop="handleReplyClick">回复</van-button>
    <CommentPost v-model:visible="show_post"></CommentPost>
  </div>
</template>

<script>
import CommentPost from '@/pages/bbs/detail/blocks/action/commentPost.vue';
export default {
  components: { CommentPost },
  props: {
    type: String
  },
  data() {
    return {
      show_post: false
    }
  },
  mounted() { },
  methods: {
    handleReplyClick() {
      this.show_post = true
    }
  },
}
</script>

<style lang="scss" scoped>
:deep(.van-button--small) {
  height: 24px;
}
</style>