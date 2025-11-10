<template>
    <!-- 车辆信息展示 -->
  <div style="padding: 0 16px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
    <div>
        <!-- <div style="font-size: 18px; font-weight: 600; color: #1a1a1a;">
            {{ formData.model }}
        </div> -->
        <div style="font-size: 14px; color: #969799; margin-top: 4px;">
            {{ formData.model }}
        </div>
    </div>
    <van-tag type="success">
        {{ formData.carType }}
    </van-tag>
</div>

    <div style="padding: 0 16px; font-size: 16px; font-weight: 500; margin-bottom: 16px; color: #323233;">
        请填写举报信息
    </div>
</template>
<script>
export default {
    props: {
        formData: Object
    }
};
</script>