<template>
  <z-action position="top" :label="statusLabel" round url="/do/put/act_apply" :fields="fields" :disabled="isDisabled"
    :data="params" @finish="getActStatus" />
</template>
<script>
export default {
  props: {
    type: String
  },
  computed: {
    params() {
      return { activityId: this.$route.query.id }
    },
    statusLabel() {
      const statusMap = {
        0: '立即报名',
        1: '已报名',
        2: '已取消',
        3: '已结束'
      }
      return statusMap[this.actStatus] || '立即报名'
    },

    isDisabled() {
      return [1, 2, 3].includes(this.actStatus)
    }
  },
  data() {
    return {
      actStatus: 0,//报名状态，0:未报名，1:已报名，2:已取消，3:已结束
      fields: [
        {
          name: 'telephone', label: '联系电话', type: 'tel',
          rules: [{ required: true, message: '请输入正确的联系电话', pattern: /^1[3-9]\d{9}$/ }]
        },
        {
          name: 'email', label: '邮箱地址', type: 'input',
          rules: [{ required: true, message: '请输入正确的邮箱地址', pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/ }]
        }
      ]
    }
  },
  created() {
    this.getActStatus()
  },
  methods: {
    getActStatus() {
      $.get({
        url: '/do/get/act_apply',
        data: { activityId: this.$route.query.id }
      }).then(res => {
        console.log(res);
        this.actStatus = res.status || 0
      })
    }
  }
}
</script>