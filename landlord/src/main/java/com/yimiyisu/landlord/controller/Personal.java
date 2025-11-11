package com.yimiyisu.landlord.controller;


import com.yimiyisu.landlord.domain.ArticleDo;
import com.yimiyisu.landlord.domain.CarDO;
import com.yimiyisu.landlord.domain.CommunityLogDO;
import com.yimiyisu.landlord.domain.LandlordHouseDO;
import com.zen.ZenController;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.AccessRole;
import com.zen.annotation.Inject;
import com.zen.enums.ZenRole;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心
 */
@AccessRole(ZenRole.SIGNATURE)
public class Personal extends ZenController {

    @Inject
    private ZenEngine zenEngine;

    // 我的车辆明细
    public ZenResult myParkingInfo(ZenData data) {
        String houseId = data.get("houseId");
        ZenResult carListResult = zenEngine.execute("list/car", data);
        if (carListResult.isEmpty()) return ZenResult.success().setData(new ArrayList<>());
        List<CarDO> carList = carListResult.asList(CarDO.class);

        // 获取车辆车位位置
        for (CarDO car : carList) {
            String parkingId = car.getParkingId(); // 车位id
            ZenResult parkingResult = zenEngine.execute("get/parking", ZenData.create(data).put("id", parkingId));
            if (parkingResult.isEmpty()) continue;
            String areaTitle = parkingResult.get("areaTitle"); // 车位区域
            String type = parkingResult.get("type"); // 车位类型
            car.setAreaTitle(areaTitle);
            car.setParkingType(type);
        }
        return ZenResult.success().setData(carList);
    }

    // 查询我的房子列表
    public ZenResult myHouse(ZenData data) {
        // 查询房屋业主关系表
        ArrayList<String> houseList = new ArrayList<>();
        ZenResult landlordHouseResult = zenEngine.execute("list/landlord_house", ZenData.create("landlordId", data.getUid()));
        if (landlordHouseResult.isEmpty()) return ZenResult.success().setData(houseList);
        List<LandlordHouseDO> landlordHouseList = landlordHouseResult.asList(LandlordHouseDO.class);

        // 获取所有houseId
        List<String> houseIdList = landlordHouseList.stream()
                .map(LandlordHouseDO::getHouseId)
                .toList();
        return ZenResult.success().setData(houseIdList);
    }

    // 我的收藏
    public ZenResult getMyCollections(ZenData data) {
        // 根据uid查询所有点赞日志
        String uid = data.getUid();
        List<CommunityLogDO> collectionLogs = new ArrayList<>();
        getAllData(0, uid, collectionLogs);

        // 根据id查询文章数据
        List<String> articleIds = collectionLogs.stream()
                .filter(log -> log != null && log.getParent() != null)
                .map(CommunityLogDO::getParent)
                .toList();
        ZenResult articleResult = zenEngine.execute("list/community_activity_in", ZenData.create("id", articleIds.toString()));
        if (articleIds.isEmpty()) return ZenResult.success().setData(new ArrayList<>());
        List<ArticleDo> articleList = articleResult.asList(ArticleDo.class);
        return ZenResult.success().setData(articleList);
    }

    // 查询用户所有收藏日志
    private void getAllData(int page, String uid, List<CommunityLogDO> data) {
        ZenResult collectionResult = zenEngine.execute("list/shequ_log_mycol", ZenData.create("uid", uid).put("page", page).put("action", "collection"));
        if (collectionResult.isEmpty()) return;
        page = page + 1;
        List<CommunityLogDO> collectionlist = collectionResult.asList(CommunityLogDO.class);
        data.addAll(collectionlist);
        getAllData(page, uid, data);
    }

}
