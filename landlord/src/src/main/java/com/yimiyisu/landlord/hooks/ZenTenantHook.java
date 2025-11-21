package com.yimiyisu.landlord.hooks;

import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.Inject;
import com.zen.annotation.ZenHook;
import com.zen.enums.ZenAction;
import com.zen.interfaces.IHook;

@ZenHook("zenTenant")
public class ZenTenantHook implements IHook {

    @Inject
    private ZenEngine zenEngine;

    @Override
    public ZenResult before(ZenData data) {
        ZenResult tenantResult = zenEngine.execute("get/latestCommunityManager", data);
        if (tenantResult.isEmpty()) return ZenResult.fail("找不到相关社区");
        return ZenResult.success().setData(tenantResult.get("communityId")).setAction(ZenAction.END);
    }
}
