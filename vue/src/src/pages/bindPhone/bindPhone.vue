<template>
    <div class="page">
        <van-icon name="sign" size="40px" class="icon" />
        <p style="text-align: center; color: #666;">请先绑定您的手机号码</p>
        <van-field v-model="phone" type="number" label="手机号" placeholder="请输入手机号" maxlength="11" />
        <van-field v-model="code" center clearable label="验证码" placeholder="请输入短信验证码">
            <template #button>
                <van-count-down v-if="time > 0" format="ss 秒后重试" :time="time" @finish="finish" />
                <van-button v-else size="small" type="primary" @click="getCode">发送验证码</van-button>
            </template>
        </van-field>
        <van-button type="primary" block @click="submit">绑定手机号</van-button>
    </div>
</template>

<script>
export default {
    data() {
        return {
            phone: '',
            code: '',
            time: 0
        };
    },
    methods: {
        // 获取验证码
        async getCode() {
            if (!this.validatePhone()) {
                return;
            }
            const { phone } = this
            await $.post({ url: '/api/auth/sendSms', data: { phone } })
            this.time = 60 * 1000;
        },
        finish() {
            this.time = 0
        },
        // 提交绑定
        async submit() {
            if (!this.validatePhone() || !this.code) {
                return;
            }
            const { phone, code } = this
            const result = await $.post({ url: '/api/auth/bindPhone', data: { phone, code } })
            $.post({ url: '/kooteam/api/home/message', data: { messageId: result } })
            this.$router.go(-1)
        },
        // 验证手机号
        validatePhone() {
            const phoneRegex = /^1[3-9]\d{9}$/;
            if (!phoneRegex.test(this.phone)) {
                $.error('请输入有效的手机号');
                return false;
            }
            return true;
        }
    },
};
</script>
<style scoped>
.page {
    padding: 20px;
}

.icon {
    display: block;
    margin: 20px auto;
    text-align: center;
}

.van-button--block {
    margin-top: 20px;
}
</style>
