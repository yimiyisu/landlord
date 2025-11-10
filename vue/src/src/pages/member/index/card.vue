<template>
    <z-block class="card" url="/api/member/info">
        <template #default="info">
            <div class="wrap">
                <div class="z-text-wrapper">
                    <z-text v-model="info.tenant" depend="community" />
                </div>
                <div class="avatar">
                    <z-csl>
                        <z-avatar :value="info.id" size="42px" />
                    </z-csl>
                </div>
                <div class="nick">
                    <div class="large" @click="apply(info.pending)">
                        {{ info.nick }}
                        <van-badge v-if="info.pending" dot>
                            （房产绑定正在审批中）
                        </van-badge>
                        <van-badge v-else-if="!isBind" dot>
                            （未绑定房产）
                        </van-badge>
                    </div>
                    <div class="time">今天是我们相遇的第<strong>{{ info.days }}</strong>天</div>
                </div>
                <div class="arrow">
                    <van-icon name="arrow" />
                </div>
            </div>
        </template>
    </z-block>
</template>
<script>
export default {
    inject: ['isBind'],
    mounted() {
        const primary = zen.color
        const startBg = $.color(primary).lighten(0.2).toHex()
        // const endBg = $.color(primary).darken(0.1).toHex()
        this.$el.style.setProperty(
            '--var-bg', `linear-gradient(60deg, ${startBg}, ${primary})`
        )
    },
    methods: {
        apply(pending) {
            if (this.isBind) {
                return
            } else if (pending) {
                return
            }
            this.$router.push('/member/apply')
        }
    },
}
</script>
<style lang="scss" scoped>
.card {
    height: 210px;
    margin-bottom: -70px;
    background: var(--var-bg);
}

.van-badge__wrapper {
    font-size: 11px;
}

.wrap {
    display: flex;
    padding-top: 25px;
    flex-wrap: wrap;
    color: #fff;
}

.z-text-wrapper {
    // 让元素宽度占满一行，实现单独一行显示
    width: 100%;
    margin-left: 20px;
    margin-bottom: 15px;
}

.avatar {
    width: 80px;
    text-align: center;
}

.large {
    font-size: 20px;
    margin-bottom: 6px;
}

.time {
    font-size: 12px;
}

strong {
    margin: 0 6px;
    font-size: 20px;
}

.nick {
    flex: 1;
}

.arrow {
    width: 30px;
}
</style>
