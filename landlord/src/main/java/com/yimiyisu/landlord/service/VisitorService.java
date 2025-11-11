package com.yimiyisu.landlord.service;

import com.yimiyisu.landlord.domain.ScanQrCodeDO;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.Component;
import com.zen.annotation.Inject;
import com.zen.kit.DateKit;

@Component
public class VisitorService {
    @Inject
    private ZenEngine zenEngine;

    public int scanQrCode(ScanQrCodeDO scanQrCodeDO) {
        ZenData zenData = ZenData.create();
        zenData.put("id", scanQrCodeDO.getId());
        //访客类型二维码
        ZenResult zenResult = zenEngine.execute("get/visitor_creator", zenData);
        System.out.println("zenResult:" + zenResult);
        if (zenResult.isEmpty()) {
            return 101;
        }
        if (zenResult.getInt("isEnter") == 1) return 100;
        long startTime = zenResult.getLong("startTime");
        long endTime = zenResult.getLong("endTime");
        long now = DateKit.now();
        if (now < startTime || now > endTime) return 102;
        if (now - scanQrCodeDO.getTime() > 3600 * 6) return 103;
        return 200;
    }
}
