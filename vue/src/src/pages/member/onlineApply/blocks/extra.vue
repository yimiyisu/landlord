<template>
    <z-form-items name="extra" :fields="fields" />
</template>

<script>
export default {
    props: {
        formData: Object
    },
    data() {
        return {
            fields: [
                { name: 'image', label: '图片', type: 'image', multiple: true },
                { name: 'note', label: '其他信息', type: 'textarea' },
                { name: 'attach', label: '附件', type: 'attach' },
            ]
        }
    },
};
</script>
