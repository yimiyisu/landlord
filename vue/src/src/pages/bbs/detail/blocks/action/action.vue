<template>
  <div>
    <div class="box flex-jasc">
      <div class="flex-aic">
        <!-- 点赞数量 -->
        <div class="gitem flex-col flex-jac" @click.stop="handleLikeCollect('likes')">
          <van-icon :name="state.likes ? 'good-job' : 'good-job-o'" size="20" :color="state.likes ? '#eb4d4b' : '#333'"
            class="gitem-icon" />
          <div class="gitem-number">{{ data.likes }}</div>
        </div>
        <!-- 收藏数量 -->
        <div class="gitem flex-col flex-jac" @click.stop="handleLikeCollect('collection')">
          <van-icon :name="state.collection ? 'star' : 'star-o'" size="20"
            :color="state.collection ? '#eb4d4b' : '#333'" class="gitem-icon" />
          <div class="gitem-number">{{ data.collection }}</div>
        </div>

        <!-- 评论数量 -->
        <div class="gitem flex-col flex-jac" @click.stop="handleCommentClick">
          <van-icon name="comment-o" size="20" color="#333" class="gitem-icon" />
          <div class="gitem-number">{{ data.comments }}</div>
        </div>

        <!-- 打赏 -->
        <div class="gitem flex-col flex-jac" @click.stop="showTipping">
          <z-action :fields="giftFields" url="/api/community/tipping" position="top" :data="giftData" @finish="refresh">
            <template #label>
              <van-icon name="point-gift-o" size="20" color="#333" class="gitem-icon" />
            </template>
          </z-action>
          <div class="gitem-number">{{ tippingCount }}</div>
        </div>
      </div>
      <!-- 立即报名 -->
      <div class="flex-aic">
        <Apply v-if="type === 'activity'" />
      </div>
    </div>
  </div>
</template>

<script>
import Apply from './apply.vue'
export default {
  components: {
    Apply
  },
  props: {
    type: String,
    data: Object,
    state: Object
  },
  data() {
    return {
      tippingCount: 0,
      giftData: {
        articleId: this.$route.query.id
      },
      giftFields: [
        {
          label: '贝壳数', name: 'type', type: 'radiobox', options: [
            { label: '赠送', value: 1 },
            { label: '扣除', value: 0 }
          ], rules: [{ required: true, message: '请选择操作' }]
        },
        { label: '贝壳数', name: 'shell', type: 'number', min: 1 },
        { label: '留言', name: 'message', type: 'select', code: 'tippingMessage' }
      ],
    }
  },
  created() {
    this.getTippingCount()
  },
  methods: {
    // 处理点赞收藏记录
    handleLikeCollect(action) {
      if (action === 'likes') {
        this.state.likes = !this.state.likes
        if (this.state.likes) {
          this.data.likes += 1
        } else {
          this.data.likes -= 1
        }
      } else {
        this.state.collection = !this.state.collection
        if (this.state.collection) {
          this.data.collection += 1
        } else {
          this.data.collection -= 1
        }
      }
      $.post({
        url: '/api/community/handleLog',
        data: {
          parent: this.$route.query.id,
          target: this.$route.query.id,
          action: action
        }
      })
    },
    //获取总打赏记录数
    getTippingCount() {
      $.get({
        url: '/do/count/tipping',
        data: { articleId: this.$route.query.id }
      }).then(res => {
        this.tippingCount = res
      })
    },
    //打开评论输入框
    handleCommentClick() {
      this.$emit('showPost', null)
    },
    //刷新打赏记录
    refresh() {
      setTimeout(() => {
        this.getTippingCount()
        this.$emit('getDetailInfo')
      }, 500)
    }
  },
}
</script>

<style lang="scss" scoped>
.box {
  z-index: 999;
  background-color: #fff;
  width: 100%;
  height: 56px;
  box-sizing: border-box;
}

.gitem {
  margin-right: 18px;

  // &-icon{}
  &-number {
    color: #333;
    font-size: 12px;
    line-height: 1;
    padding-top: 4px;
  }
}

:deep(.van-button) {
  height: 32px;
}
</style>
