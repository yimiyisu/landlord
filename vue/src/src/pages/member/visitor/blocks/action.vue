<template>
    <z-action v-if="!item.isEnter" position="top" size="small" url="/api/visitor/put" :data="item" style="margin: 10px" label="编辑"
        :fields="fields" />
    <z-action size="small" label="添加访客" url="/do/put/visitor_cont" mode="confirm" style="margin: 10px"
        :data="{ title: item.realname, telephone: item.telephone, licensePlate: item.licensePlate }"
        :beforeSubmit="beforeSubmit" />
    <z-action size="small" v-if="!item.isEnter" style="margin: 10px" label="访客码" mode="dialog">
        <z-block url="/api/visitor/visitorQr" :params="item">
            <template #default="qrCode">
                <z-qr :value="qrCode" />
                <span style="font-size: small; display: block; text-align: center;">
                    有效期<br />
                    <z-date :value="item.startTime" plain="true" />至
                    <z-date :value="item.endTime" plain="true" />
                </span>
            </template>
        </z-block>
    </z-action>
    <z-action v-if="!item.isEnter" size="small" url="/do/delete/visitor" :data="{ id: item.id }" style="margin: 10px"
        label="取消" mode="confirm" />
</template>

<script>
export default {
    props: {
        item: Object,
        fields: Array
    },
    methods: {
        async beforeSubmit(data) {
            const res = await $.get({
                url: '/do/get/visitor_cont',
                data: data
            });
            if (res) {
                $.fail('该访客已添加');
                return false;
            }
        }
    },
}
</script>
