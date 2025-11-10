<template>
    <van-cell title="活动地址">
        <template #label>
            {{ addressInfo.label || '--' }}
        </template>
        <template #value>
            <z-action title="位置选择" width="720px" :beforeShow="show" :beforeSubmit="submit" size="mini">
                <div class="search-box">
                    <van-field v-model="keyword" input-align="left" placeholder="输入关键字搜索位置" @keyup.enter="change">
                        <template #button>
                            <van-button size="small" type="primary" @click="change">搜索</van-button>
                        </template>
                    </van-field>
                </div>
                <div id="my-panel"></div>
                <div id="mapView" style="height:300px"></div>
                <div style="margin:12px;font-weight:bold">{{ addressInfo.label }}</div>
            </z-action>
        </template>
    </van-cell>
</template>
<script>
export default {
    data() {
        return {
            addressInfo: { label: '' },
            placeSearch: null,  // 地点搜索对象
            keyword: '', // 搜索关键词
        }
    },
    computed: {
        label() {
            return this.addressInfo.label || '点击选择位置'
        }
    },
    methods: {
        async show() {
            await this.load();  // 等待高德地图API加载完成
            setTimeout(() => {
                this.initMap()
            }, 200);
        },
        async load() {
            if (!window.AMap) {
                window._AMapSecurityConfig = {
                    securityJsCode: "72c99c783b2e0b01fa30ff01e76c9192",
                }
                const script = document.createElement('script');
                script.src = "https://webapi.amap.com/maps?v=2.0&key=d276203949b866fb4e5a9b73ed8bfda8&plugin=AMap.Geocoder,AMap.Geolocation,AMap.ToolBar,AMap.PlaceSearch";
                document.head.appendChild(script);
                await new Promise(resolve => script.onload = resolve);
            }
            return window.AMap;
        },
        change() {
            this.placeSearch.search(this.keyword);
        },
        initMap() {
            const { AMap } = window
            const map = new AMap.Map('mapView', {
                resizeEnable: true, // 是否监控地图容器尺寸变化
                zoom: 8 // 初始地图缩放级别
            });
            // 获取用户当前位置
            const geolocation = new AMap.Geolocation({
                enableHighAccuracy: true, // 是否使用高精度定位，默认:true
                timeout: 10000, // 超过10秒后停止定位，默认：无穷大
                buttonOffset: new AMap.Pixel(10, 20), // 定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
                zoomToAccuracy: true, // 定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
                buttonPosition: 'RB'
            });
            map.addControl(geolocation);
            geolocation.getCurrentPosition(() => {
                const { position } = this.addressInfo
                if (position) {
                    map.setCenter(position);
                }
            });
            AMap.plugin(["AMap.PlaceSearch"], () => {
                const placeSearch = new AMap.PlaceSearch({
                    pageSize: 3, //单页显示结果条数
                    pageIndex: 1, //页码
                    map: map, //展现结果的地图实例
                    panel: "my-panel",
                    autoFitView: true, //是否自动调整地图视野使绘制的 Marker 点都处于视口的可见范围,
                });
                placeSearch.search("");
                this.placeSearch = placeSearch
            });
            // 为地图添加点击事件监听
            map.on("click", (e) => {
                const position = e.lnglat;
                const geocoder = new AMap.Geocoder({
                    radius: 1000, // 范围，默认：500
                });
                geocoder.getAddress(position, (status, result) => {
                    if (status === 'complete' && result.info === 'OK') {
                        this.addressInfo = { position, label: result.regeocode.formattedAddress }
                    } else {
                        $.error('获取地址失败')
                    }
                });
            });
        },
        submit() {
            const { addressInfo } = this;
            // 更新父组件的值
            // debugger
            this.$emit('update:modelValue', addressInfo);
        },
    },
}
</script>
<style>
.search-box {
    margin: 10px 0 -1px 0;
}

#mapView {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 0;
}

#my-panel {
    position: absolute;
    width: 240px;
    overflow: hidden;
    right: 0;
    z-index: 1;
}
</style>
