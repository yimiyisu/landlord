package com.yimiyisu.landlord.controller;

import com.zen.ZenController;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.AccessRole;
import com.zen.annotation.Inject;
import com.zen.enums.ZenRole;
import com.zen.kit.JsonKit;
import com.zen.kit.StringKit;

/**
 * 访客
 */
@AccessRole(ZenRole.SIGNATURE)
public class Visitor extends ZenController {
    @Inject
    private ZenEngine zenEngine;

    //业主预约访客
    public ZenResult put(ZenData data) {
        long startTime = 0;
        int time = Integer.parseInt(data.get("time"));
        int timeRange = Integer.parseInt(data.get("timeRange"));
        startTime = switch (timeRange) {
            case 1 -> time - 2 * 3600;
            case 2 -> time + 4 * 3600;
            case 3 -> time + 10 * 3600;
            default -> time;
        };
        long endTime = startTime + 6 * 3600;
        data.put("startTime", startTime);
        data.put("endTime", endTime);
        if (data.get("id") == null || data.get("id").isEmpty()) {
            data.put("type", 1);
            zenEngine.execute("put/visitor", data);
            return ZenResult.success();
        }
        zenEngine.execute("patch/visitor", data);
        return ZenResult.success();
    }

    public ZenResult patch(ZenData data) {
        zenEngine.execute("patch/visitor", data);
        return ZenResult.success();
    }

    //生成访客二维码
    public ZenResult visitorQr(ZenData data) {
        ZenResult visitorResult = zenEngine.execute("get/visitor_cont", data);
        if (visitorResult.isEmpty()) {
            ZenResult zenResult = zenEngine.execute("get/visitor_creator", data);
            String creator = zenResult.get("creator");
            if (creator == null || creator.isEmpty() || !creator.equals(data.getUid())) {
                return ZenResult.fail("二维码生成失败，访客数据错误");
            }
        }
        String stringify = JsonKit.stringify(data.getId());
        String encrypt = StringKit.encrypt("fk_" + stringify);// 加密后信息
        return ZenResult.success().setData(encrypt);
    }


}
