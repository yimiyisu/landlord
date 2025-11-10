<template>
    <van-popup round v-model:show="show" position="bottom">
        <div class="box flex-jasc">
            <textarea v-model="reply_content" class="textarea flex-grow" :placeholder="placeholder"></textarea>
            <van-button type="primary" size="small" @click.stop="handleSubmitClick"> 提交</van-button>
        </div>
    </van-popup>
</template>
<script>
export default {
    props: {
        visible: Boolean,
        parentComment: Object
    },

    data() {
        return {
            reply_content: ''
        }
    },
    computed: {
        show: {
            get() {
                return this.visible
            },
            set(val) {
                this.$emit('update:visible', val)
            }
        },
        placeholder() {
            return '请输入内容'
        }
    },
    methods: {
        // 提交评论 、回复操作
        handleSubmitClick() {
            $.post({
                url: "/do/put/message",
                data: {
                    articleId: this.$route.query.id,
                    content: this.reply_content,
                    replyContent: this.parentComment ? {
                        id: this.parentComment.id,
                        uid: this.parentComment.creator,
                        content: this.parentComment.content
                    } : null
                }
            }).then(() => {
                // 直接通过父组件访问兄弟组件的ref，刷新评论列表
                this.$parent.$refs.commentList.refresh()
                //刷新评论数量
                this.$emit('getDetailInfo')
                this.show = false
                this.reply_content = ''
            })
        }
    }
}
</script>

<style lang="scss" scoped>
.box {
    padding: 20px 14px;
    box-sizing: border-box;
    padding-bottom: 30px;
}

.textarea {
    width: 100%;
    background: #eee;
    margin-right: 10px;
    border-radius: 8px;
    border: none;
    resize: none;
    height: 60px;
    padding: 10px;
    box-sizing: border-box;
    line-height: 20px;
}
</style>
