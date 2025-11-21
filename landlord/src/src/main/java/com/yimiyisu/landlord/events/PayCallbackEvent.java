package com.yimiyisu.landlord.events;

import com.google.common.eventbus.Subscribe;
import com.yimiyisu.landlord.events.model.PayCallbackEventModel;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.annotation.Inject;
import com.zen.domain.PayDO;
import com.zen.interfaces.IEvent;
import com.zen.kit.PayKit;

// 业务支付回调事件
public class PayCallbackEvent implements IEvent<PayCallbackEventModel> {

    @Inject
    private ZenEngine zenEngine;

    @Override
    @Subscribe
    public void execute(PayCallbackEventModel payCallbackEventModel) {
        String payId = payCallbackEventModel.getPayId(); // 支付id
        boolean finished = PayKit.isFinished(payId); // 支付是否完成
        PayDO payDO = PayKit.get(payId); // 支付对象
        if (!finished) return;
        // 修改业务信息
        String scene = payDO.getScene(); // 支付场景
        String url = "patch/" + scene + "_log";
        zenEngine.execute(url, ZenData.create("id", payDO.getOrderId())
                .put("status", 2).put("tradeId", payDO.getTradeId()).put("payTime", payDO.getUpdateGmt()));
    }
}
