<template>
  <div class="service-item">
    <div class="flex-aic">
      <van-image class="avatar" :src="defaultAvatar" width="40" height="40" round></van-image>
      <div class="flex-grow">
        <div class="title">xxx服务</div>
        <div class="time">2025-05-13 10:18:02</div>
      </div>
    </div>
    <div class="content">服务内容服务内容服务内容服务内容服务内容服务内容服务内容服务内容服务内容服务内容服务内容</div>
  </div>
</template>

<script>
export default {
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
.service-item{line-height: 1; margin-bottom: 10px; padding: 16px 14px; background-color: #fff; box-sizing: border-box;
  .avatar{margin-right: 10px;}
  .title{font-size: 14px; color: #444; font-weight: bold; padding: 4px 0; padding-bottom: 8px;}
  .time{font-size: 12px; color: #999;}
  .content{padding-top: 12px; box-sizing: border-box; font-size: 14px; line-height: 18px; color: #555;}

}
</style>