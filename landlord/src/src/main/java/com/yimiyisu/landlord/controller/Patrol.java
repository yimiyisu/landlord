package com.yimiyisu.landlord.controller;

import com.zen.ZenController;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.AccessRole;
import com.zen.annotation.Inject;
import com.zen.enums.ZenRole;

/**
 * 巡检
 */
@AccessRole(ZenRole.SIGNATURE)
public class Patrol extends ZenController {
    @Inject
    private ZenEngine zenEngine;

    public ZenResult put(ZenData data){
        //查询物业人员id
        ZenResult execute = zenEngine.execute("get/community_manager",data);
        if (execute.isEmpty()) return ZenResult.fail("无该物业人员");
        data.put("recipientId",execute.get("id"));
        zenEngine.execute("put/patrol_log",data);
        return ZenResult.success();
    }
}
