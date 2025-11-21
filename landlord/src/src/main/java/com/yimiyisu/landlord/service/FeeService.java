package com.yimiyisu.landlord.service;

import com.yimiyisu.landlord.domain.FeePlainDO;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.Component;
import com.zen.annotation.Inject;
import com.zen.kit.DateKit;
import com.zen.kit.StringKit;

import java.util.List;

@Component
public class FeeService {

    @Inject
    private ZenEngine zenEngine;

    // 根据类型和feeId 查找费率配置
    public FeePlainDO.FeeConfig getFeeConfig(String fee, String type) {
        ZenResult feeConfigResult = zenEngine.execute("get/fee_plain", ZenData.create("id", fee));
        FeePlainDO feePlain = feeConfigResult.asEntity(FeePlainDO.class);
        if (feePlain.getStatus() == 0 || feePlain.getStartTime() > DateKit.now())
            return null;
        List<FeePlainDO.FeeConfig> feeConfigs = feePlain.getConfigs();

        // 找出该车位的费率配置
        for (FeePlainDO.FeeConfig item : feeConfigs) {
            if (StringKit.equals(item.getTitle(), type)) {
                return item;
            }
        }
        return null;
    }
}
