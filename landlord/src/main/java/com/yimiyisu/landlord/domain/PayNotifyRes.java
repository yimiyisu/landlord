package com.yimiyisu.landlord.domain;

import lombok.Data;

import java.util.Date;

// 支付回调微信返回数据解密后
@Data
public class PayNotifyRes {
    private String transaction_id;
    private String out_trade_no;
    private Amount amount;
    private String mchid;
    private String appid;
    private String trade_state;
    private Date success_time;

    @Data
    public static class Amount{
        private long total;
        private long payer_total;
        private String currency;
        private String payer_currency;
    }
}
