<template>
  <CreatorInfo :detailInfo="detailInfo" />

  <div class="flex-jasc priceinfo">
    <!-- 二手 -->
    <div class="price flex-aic" v-if="detailInfo.marketType === 'product'">
      <div class="now">￥{{ detailInfo.curPrice / 100 || 0 }}</div>
      <div class="origin">￥{{ detailInfo.oriPrice / 100 || 0 }}</div>
    </div>
    <!-- 租赁 -->
    <div class="price flex-aic" v-if="detailInfo.marketType === 'lease'">
      <div class="now">￥{{ detailInfo.rent / 100 || 0 }}</div>
      <van-tag type="primary" class="space">
        <z-dict code="depositType" :modelValue="detailInfo.depositType" />
      </van-tag>
    </div>
    <!-- 小生意 -->
    <div class="price flex-aic" v-if="detailInfo.marketType === 'dicker'">
      <div class="now">￥{{ detailInfo.unitPrice / 100 || 0 }}</div>
      <div class="space stock">每单限购: {{ detailInfo.limit }}</div>
      <div class="space stock">总库存: {{ detailInfo.stock }}</div>
    </div>
    <div class="want">{{ detailInfo.collection }}人想要</div>

  </div>
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
  },
}
</script>
<style>
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

.priceinfo {
  padding-top: 14px;
  line-height: 1;

  .price {
    align-items: end;

    .now {
      color: #FE5100;
      font-size: 18px;
      font-weight: bold;
    }

    .origin {
      font-size: 12px;
      color: #999;
      padding-bottom: 2px;
      text-decoration: line-through;
      padding-left: 10px
    }
  }

  .want {
    color: #999;
    font-size: 12px;
  }
}

.space {
  margin-left: 10px;
  margin-bottom: 3px;
}

.stock {
  color: #999;
  font-size: 12px;
}
</style>