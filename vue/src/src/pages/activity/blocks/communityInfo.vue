<template>
  <div>
    <div class="flex-aic" @click.stop="handleClick">
      <!-- 显示一张主图 -->
      <van-image class="main-image" :src="testImg" lazy width="50" height="50"></van-image>
      <div class="flex-grow">
        <div class="nickname">发布人的昵称</div>
        <div class="content">发布的内容发布的内容发布的内容发布的内容发布的内容发布的内容发布的内容发布的内容发布的内容发布的内容发布的内容发布的内容</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data(){
    return {
      testImg: 'http://gips0.baidu.com/it/u=1490237218,4115737545&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=720',
    }
  },
  mounted() {},
  methods:{
    handleClick() {
      // 跳转到动态详情
      //this.$router.push({path: `/bbs/detail?id=`})
    }
  },
}
</script>

<style lang="scss" scoped>
.main-image{margin-right: 10px;}
.nickname{font-size: 14px; font-weight: bold; color: #333; line-height: 1; padding-bottom: 10px;}
.content{font-size: 14px; line-height: 1; color: #999; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;}
</style>
