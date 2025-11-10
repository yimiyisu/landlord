<template>
  <div class="item">
    <!-- 评论人、点赞人、关注人 信息-->
    <div class="flex-jasc">
      <div class="flex-aic">
        <van-image class="avatar" :src="defaultAvatar" width="40" height="40" round></van-image>
        <div>
          <div class="nickname">操作人的昵称</div>
          <div class="time">2025-05-13 09:45:24</div>
        </div>
      </div>
      <Action :type="type"></Action>
    </div>
    <!-- 操作类型 -->
    <div class="operation">
      <!-- 点赞 -->
      <div v-if="type == 'like'" class="flex-aic">赞了这条动态</div>
      <div v-if="type == 'comment'" class="flex-aic">这是评论、回复的内容</div>
      <div v-if="type == 'collect'" class="flex-aic">收藏了这条动态</div>
      <div v-if="type == 'follow'" class="flex-aic">关注了你</div>
    </div>
    <!-- 动态信息 -->
    <communityInfo v-if="type != 'follow'" :type="type"></communityInfo>
  </div>
</template>

<script>
import communityInfo from './communityInfo.vue';
import Action from './action.vue';
export default {
  components: { communityInfo, Action },
  props: {
    type: String,
    data: Object
  },
  data(){
    return {
      defaultAvatar: 'https://ww2.sinaimg.cn/mw690/6f833727gy1htsvgzfbmpj218g18gdk0.jpg',
    }
  },
  mounted() {},
  methods:{},
}
</script>

<style lang="scss" scoped>
.item{background-color: #fff; margin-bottom: 10px; padding: 14px; box-sizing: border-box; line-height: 1;}
.avatar{margin-right: 10px;}
.nickname{font-size: 14px; color: #333; padding-top: 4px; padding-bottom: 6px; font-weight: bold; line-height: 1;}
.time{font-size: 12px; color: #999; line-height: 1;}
.operation{padding: 14px 0; color: #333; font-size: 14px; line-height: 1;}
</style>