package com.yimiyisu.landlord.controller;

import com.yimiyisu.landlord.domain.AreaDo;
import com.yimiyisu.landlord.domain.HouseDo;
import com.yimiyisu.landlord.domain.LandlordHouseDO;
import com.zen.ZenController;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.AccessRole;
import com.zen.annotation.Inject;
import com.zen.domain.ZenUser;
import com.zen.enums.ZenRole;
import com.zen.kit.DateKit;
import com.zen.kit.StringKit;
import com.zen.kit.UserKit;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AccessRole(ZenRole.SIGNATURE)
public class Member extends ZenController {

    private final static String APPLY_KEY = "houseApply";
    @Inject
    private ZenEngine zenEngine;

    public ZenResult info(ZenData data) {
        ZenUser user = data.getUser();
        Map<String, Object> result = new HashMap<>();
        result.put("nick", user.getNick());
        if (user.hasMeta(Member.APPLY_KEY)) result.put("pending", true);
        long created = DateKit.now() - DateKit.idToTime(user.getId());
        result.put("days", created / (24 * 3600));
        result.put("id", user.getId());
        result.put("tenant",data.getTenant());
        return ZenResult.success().setData(result);
    }

    // 获取属性房屋结构数据并附带房屋信息
    public ZenResult getHouseList(ZenData data) {
        String communityId = data.getTenant(); // 当前小区id
        ArrayList<HouseDo> houseList = new ArrayList<>(); // 所有房屋
        if (!data.isEmpty("isAll")){
            getUserHouse(0,houseList, data.getUid());
            return ZenResult.success().setData(houseList);
        }
        List<AreaDo> areaList = areaList(communityId); // 小区下所有房屋区域属性结构集合
        getAllHouse(0, houseList);
        // 将房屋挂载到区域中
        attachHousesToAreas(areaList, houseList);

        return ZenResult.success().setData(areaList);
    }

    // 申请入住
    public ZenResult apply(ZenData data) {
        // 通过meta字段判断当前用户是否正在申请
        ZenUser user = data.getUser();
        if (user.hasMeta(Member.APPLY_KEY)) return ZenResult.success("申请正在审批中，请稍后再试");

        // 没有申请 - 提交申请 -> 填写meta字段标记
        ZenData applyData = ZenData.create(data);
        String title = data.get("title");
        String telephone = data.get("telephone");
        String idcard = data.get("idcard");
        String houseId = data.get("houseId");
        if (StringKit.isEmpty(houseId)) return ZenResult.fail("请选择房产");

        // 提交申请
        zenEngine.execute("put/landlord_apply", applyData.put("title", title).put("telephone", telephone).put("idcard", idcard).put("houseId", houseId));
        // 填写meta字段
        UserKit.setMeta(user.getId(), Member.APPLY_KEY, "1");

        return ZenResult.success();
    }

    // 将房屋挂载到区域之下
    private void attachHousesToAreas(List<AreaDo> areaTree, List<HouseDo> houses) {
        // 1. 创建ID到AreaDo的映射表（包含所有层级节点）
        Map<String, AreaDo> areaMap = new HashMap<>();
        Queue<AreaDo> queue = new LinkedList<>(areaTree);
        while (!queue.isEmpty()) {
            AreaDo current = queue.poll();
            areaMap.put(current.getId(), current);
            if (current.getChildren() != null) queue.addAll(current.getChildren());
        }

        // 2. 将房屋按区域ID分组
        Map<String, List<HouseDo>> housesByArea = houses.stream()
                .collect(Collectors.groupingBy(HouseDo::getAreaId));

        // 3. 将房屋挂载到对应区域节点
        for (Map.Entry<String, List<HouseDo>> entry : housesByArea.entrySet()) {
            AreaDo parentArea = areaMap.get(entry.getKey());
            if (parentArea == null) continue;

            // 如果当前区域是叶子节点，更新为非叶子节点
            if (parentArea.getIsLeaf() == 1) parentArea.setIsLeaf(0);

            // 转换房屋为AreaDo节点
            List<AreaDo> houseNodes = entry.getValue().stream()
                    .map(house -> new AreaDo(
                            house.getId(),
                            house.getTitle(),
                            parentArea.getId(), // parentId = 当前区域ID
                            1,                // 房屋始终是叶子节点
                            null  // 房屋没有子节点
                    ))
                    .toList();

            // 添加到当前区域的children列表
            parentArea.getChildren().addAll(houseNodes);
        }
    }

    // 递归获取所有房屋
    private void getAllHouse(int page, List<HouseDo> houseList) {
        // 所有房屋
        ZenResult houseResult = zenEngine.execute("list/house", ZenData.create().put("page", page));
        if (houseResult.isEmpty()) return;
        List<HouseDo> houseDoList = houseResult.asList(HouseDo.class);
        houseList.addAll(houseDoList);
        page = page + 1;
        getAllHouse(page, houseList);
    }

    // 递归获取业主房屋
    private void getUserHouse(int page, List<HouseDo> houseList,String uid){
        ZenResult zenResult = zenEngine.execute("list/landlord_house", ZenData.create("landlordId", uid).put("page", page));
        if (zenResult.isEmpty()) return;
        List<LandlordHouseDO> landlordHouseDOList = zenResult.asList(LandlordHouseDO.class);
        for (LandlordHouseDO landlordHouseDO : landlordHouseDOList) {
            ZenResult houseResult = zenEngine.execute("get/house", ZenData.create("id", landlordHouseDO.getHouseId()));
            houseList.add(houseResult.asEntity(HouseDo.class));
        }
        page = page + 1;
        getUserHouse(page, houseList, uid);
    }

    // 查询小区的所有住宅区并构建为树形结构
    private List<AreaDo> areaList(String communityId) {
        // 查询所有住宅区域
        ZenData queryData = ZenData.create("communityId", communityId);
        ZenResult commerceAreas = zenEngine.execute("list/house_area", queryData);
        if (commerceAreas.isEmpty()) return new ArrayList<>();
        List<AreaDo> areaDoList = commerceAreas.asList(AreaDo.class);

        // 构建属性结构
        Map<String, AreaDo> areaMap = areaDoList.stream()
                .collect(Collectors.toMap(AreaDo::getId, Function.identity()));
        List<AreaDo> roots = new ArrayList<>();
        for (AreaDo areaDo : areaDoList)
            if (areaDo.getParentId().equals("0")) roots.add(areaDo);
            else
                Optional.ofNullable(areaMap.get(areaDo.getParentId())).ifPresent(parent -> parent.getChildren().add(areaDo));

        return roots;
    }
}
