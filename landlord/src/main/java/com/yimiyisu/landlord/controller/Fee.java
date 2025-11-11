package com.yimiyisu.landlord.controller;

import com.yimiyisu.landlord.domain.CarDO;
import com.yimiyisu.landlord.domain.FeePlainDO;
import com.yimiyisu.landlord.domain.WaterDO;
import com.yimiyisu.landlord.service.FeeService;
import com.zen.*;
import com.zen.annotation.AccessRole;
import com.zen.annotation.Inject;
import com.zen.enums.ZenRole;
import com.zen.kit.DateKit;
import com.zen.kit.StringKit;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AccessRole(ZenRole.SIGNATURE)
public class Fee extends ZenController {

    @Inject
    private ZenEngine zenEngine;

    @Inject
    private FeeService feeService;

    // 将时间戳转为日期直接加上月份
    private static long addMonthsToTimestamp(long timestamp, int monthsToAdd) {
        ZonedDateTime dateTime = Instant.ofEpochSecond(timestamp)
                .atZone(ZoneOffset.UTC);
        ZonedDateTime newDateTime = dateTime.plusMonths(monthsToAdd);
        return newDateTime.toEpochSecond();
    }

    // 新增水/电费管理记录
    public ZenResult addWaterLog(ZenData data) {
        String feeId = data.get("feeId"); // 费率配置id
        String type = data.get("type"); // 类型

        double number = Double.parseDouble(data.get("number")); // 本次用量

        // 查询设备信息
        ZenResult equipmentResult = zenEngine.execute("get/equipment", ZenData.create("number", data.get("equipment")));
        if (equipmentResult.isEmpty()) return ZenResult.fail("请输入正确的设备编号");
        int equipmentType = equipmentResult.getInt("type");

        // 查询费率配置
        FeePlainDO.FeeConfig feeConfig = feeService.getFeeConfig(feeId, type); // 费率配置
        if (feeConfig == null) return ZenResult.fail("没有此费率标准");

        // 计算金额
        long price = feeConfig.getPrice(); // 单价
        long cash = (long) (price * number);

        if (equipmentType == 4) {
            zenEngine.execute("patch/water_log", data.put("cash", cash));
        } else if (equipmentType == 5) {
            zenEngine.execute("put/electric_log", data.put("cash", cash));
        }

        return ZenResult.success();
    }

    // 水电 支付/退款
    public ZenResult hydropowerPay(ZenData data) {
        // 创建ZenPay
        String orderId = data.get("id");
        String type = data.get("type");
        String action = data.get("action"); // 支付/退款
        ZenResult logResult = zenEngine.execute("get/" + type + "_log", ZenData.create().put("id", orderId));
        if (logResult.isEmpty()) return ZenResult.fail("账单不存在");
        long amount = logResult.getLong("cash");
        ZenPay zenPay = new ZenPay(orderId, amount, "yimiyisu_official");
        zenPay.setScene(type);

        if (StringKit.equals(action, "pay")) {
            // 创建支付单
            String payLogId = zenPay.create(data.getUid());
            // 将payLogId回填至业务表中
            zenEngine.execute("patch/" + type + "_log", ZenData.create("id", orderId).put("payId", payLogId));
            return ZenResult.success().setData(payLogId);
        } else if (StringKit.equals(action, "refund")) {
            String payLogId = zenPay.refound(data.getUid());
            return ZenResult.success().setData(payLogId);
        }

        return ZenResult.fail("操作场景错误");
    }

    // 创建租赁记录
    public ZenResult rentParking(ZenData data) {
        int month = Integer.parseInt(data.get("num")); // 租赁月份
        // 查询车辆信息
        ZenResult carResult = zenEngine.execute("get/car", data);
        if (carResult.isEmpty()) return ZenResult.fail("车辆信息不存在");
        CarDO car = carResult.asEntity(CarDO.class);
        String parkingId = car.getParkingId();

        // 查询车位信息
        ZenResult parkingResult = zenEngine.execute("get/parking", ZenData.create("id", parkingId));
        if (parkingResult.isEmpty()) return ZenResult.fail("车位信息不存在");
        String parkingType = parkingResult.get("type"); // 车位类型

        // 查询费率配置
        FeePlainDO.FeeConfig feeConfig = feeService.getFeeConfig(car.getFee(), parkingType);
        if (feeConfig == null) return ZenResult.fail("收费标准不存在，请联系物业人员");

        // 计算金额
        long price = feeConfig.getPrice(); // 单价
        long cash = price * month;
        // 计算到期时间
        long newEndTime = 0;
        long endTime = car.getEndTime();
        if (endTime == 0) {
            newEndTime = addMonthsToTimestamp(DateKit.now(), month);
        } else {
            newEndTime = addMonthsToTimestamp(endTime, month);
        }

        // 插入记录
        zenEngine.execute("put/car_log", ZenData.create(data)
                .put("carId", data.get("id")).put("parkingId", car.getParkingId()).put("cash", cash).put("month", month).put("validTime", newEndTime));
        return ZenResult.success();
    }

    // 根据车辆id查询车位收费规则
    public ZenResult payConfig(ZenData data) {
        // 查询车辆信息
        ZenResult carResult = zenEngine.execute("get/car", data);
        if (carResult.isEmpty()) return ZenResult.fail("车辆信息不存在");
        CarDO car = carResult.asEntity(CarDO.class);
        String parkingId = car.getParkingId();

        // 查询车位信息
        ZenResult parkingResult = zenEngine.execute("get/parking", ZenData.create("id", parkingId));
        if (parkingResult.isEmpty()) return ZenResult.fail("车位信息不存在");
        String parkingType = parkingResult.get("type"); // 车位类型

        FeePlainDO.FeeConfig feeConfig = feeService.getFeeConfig(car.getFee(), parkingType);
        if (feeConfig == null) return ZenResult.fail("收费标准不存在，请联系物业人员");
        return ZenResult.success().setData(feeConfig);
    }

    public ZenResult queryWater(ZenData data){
        ZenResult execute = zenEngine.execute("list/water_log", data);
        List<WaterDO> waterDOList = execute.asList(WaterDO.class);
        Optional<WaterDO> result = waterDOList.stream()
                .filter(water -> water.getStatus() == 2)  // 过滤 status=2
                .max(Comparator.comparingInt(WaterDO::getNumber));  // 找出 number 最大的

        if (result.isPresent()) {
            WaterDO target = result.get();
            // 使用找到的对象
            System.out.println("Found: ID=" + target.getId() + ", Number=" + target.getNumber());
            return ZenResult.success().setData(target);
        } else {
            System.out.println("未找到符合条件的记录");
            return ZenResult.success().setData(new WaterDO());
        }
    }

}
