package com.yimiyisu.landlord.service;

import com.yimiyisu.landlord.domain.PayNotifyRes;
import com.yimiyisu.landlord.kit.WeixinKit;
import com.zen.kit.ConfigKit;
import com.zen.kit.DateKit;
import com.zen.kit.JsonKit;
import com.zen.kit.StringKit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

public class WxPayClient {

    private static WxPayClient wxPayConfig = null;
    private final String appId;
    private final String mchId;
    private final String notifyUrl;
    private final String keyPath;
    private final String mchSerialNo;
    private final static String OUT_TRADE_NO_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/";
    private final static String TRANSACTION_ID_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/id/";


    public WxPayClient() {
        this.appId = ConfigKit.get("wxSubcribeId");
        this.notifyUrl = ConfigKit.get("wxpay_notify_url") + "/api/home/orderCallback";
        this.mchId = ConfigKit.get("wxpay_v3_mchId");
        this.keyPath = ConfigKit.get("wxpay_v3_keyPath");
        this.mchSerialNo = ConfigKit.get("wxpay_v3_mchSerialNo");
    }


    public static WxPayClient get() {
        if (WxPayClient.wxPayConfig == null) {
            WxPayClient.wxPayConfig = new WxPayClient();
            return WxPayClient.wxPayConfig;
        }
        return WxPayClient.wxPayConfig;
    }


    /**
     * 关单
     * 官方文档：https://pay.weixin.qq.com/doc/v3/merchant/4012791860
     * @param outTradeNo 商户订单号
     */
    public boolean closeOrder(String outTradeNo) throws IOException, InterruptedException {
        // 构造请求URL
        String url = String.format(
                "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s/close", outTradeNo);

        // 构造请求体
        String requestBody = String.format("{\"mchid\":\"%s\"}", this.mchId);

        // 构造签名
        String nonceStr = StringKit.rand(32);
        long timeStamp = DateKit.now();
        String requestPath = url.replace("https://api.mch.weixin.qq.com", "");
        String signature = WeixinKit.getPaySignature("POST", requestPath, String.valueOf(timeStamp), nonceStr, requestBody);

        // 构造Authorization
        String token = String.format(
                "mchid=\"%s\",nonce_str=\"%s\",timestamp=\"%d\",serial_no=\"%s\",signature=\"%s\"",
                this.mchId, nonceStr, timeStamp, mchSerialNo, signature
        );
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;

        // 发送请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authorization)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            String body = response.body();
            System.out.println("关单失败：" + body);
            return false;
        }
        return true;
    }

    /**
     * 根据微信支付单号查询订单信息
     * 官方文档：https://pay.weixin.qq.com/doc/v3/merchant/4012791858
     * @param transactionId 微信支付单号
     */
    public PayNotifyRes queryOrderByTransactionId(String transactionId) throws Exception {
        HttpResponse<String> response = queryOrder(transactionId, TRANSACTION_ID_URL);
        if (response.statusCode() != 200) return null;
        return JsonKit.parse(response.body(), PayNotifyRes.class);
    }

    /**
     * 根据商户订单号查询订单信息
     * 官方文档：https://pay.weixin.qq.com/doc/v3/merchant/4012791859
     * @param outTradeNo 商户订单号
     */
    public PayNotifyRes queryOrderByOutTradeNo(String outTradeNo) throws Exception {
        HttpResponse<String> response = queryOrder(outTradeNo, OUT_TRADE_NO_URL);
        if (response.statusCode() != 200) return null;
        return JsonKit.parse(response.body(), PayNotifyRes.class);
    }

    /**
     * JSAPI/小程序下单(获取预支付订单 prepay_id)
     * 官网地址：https://pay.weixin.qq.com/doc/v3/merchant/4012791856
     * 注：在获取签名时的body，必须与发送请求获取prepay_id的body一模一样(包括格式)，否则会导致验签失败
     * @param map 请求体参数：description：商品描述，out_trade_no：系统订单号，notify_url：支付回调地址，amount：金额{"total": 10,"currency":"CNY"}，payer：openid{"openid":"1238128768"}
     * @return prepay_id
     */
    public String jsApiPay(Map<String, Object> map) throws Exception {
        // 获取签名
        long timeStamp = DateKit.now(); // 当前时间戳
        String nonceStr = StringKit.rand(32); // 随机字符串
        map.put("appid", this.appId);
        map.put("mchid", this.mchId);
        String body = JsonKit.stringify(map);
        String signature = WeixinKit.getPaySignature("POST", "/v3/pay/transactions/jsapi", timeStamp + "", nonceStr, body);

        // 发送请求获取prepay_id
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi"))
                .header("Authorization", "WECHATPAY2-SHA256-RSA2048 " +
                        "mchid=\"" + this.mchId + "\"," +
                        "nonce_str=\"" + nonceStr + "\"," +
                        "signature=\"" + signature + "\"," +
                        "timestamp=\"" + timeStamp + "\"," +
                        "serial_no=\"" + this.mchSerialNo + "\"")
                .header("Content-Type", "application/json").header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonKit.stringify(map)))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            return null;
        }
        String prepayId = JsonKit.parse(response.body()).getAsJsonObject().get("prepay_id").getAsString();
        return prepayId;
    }

    /**
     * 根据商户订单号/微信支付单号查询订单信息
     * @param orderNo 商户订单号/微信支付单号
     * @param basicUrl 查询路径
     */
    private HttpResponse<String> queryOrder(String orderNo, String basicUrl) throws Exception {
        // 构造请求URL
        String url = basicUrl + orderNo + "?mchid=" + this.mchId;

        // 构造签名
        String nonceStr = StringKit.rand(32);
        long timeStamp = DateKit.now();
        String requestPath = url.replace("https://api.mch.weixin.qq.com", "");
        String signature = WeixinKit.getPaySignature("GET", requestPath, String.valueOf(timeStamp), nonceStr, null);

        // 构造Authorization
        String token = String.format(
                "mchid=\"%s\",nonce_str=\"%s\",timestamp=\"%d\",serial_no=\"%s\",signature=\"%s\"",
                this.mchId, nonceStr, timeStamp, mchSerialNo, signature
        );
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;

        // 发送请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authorization)
                .header("Accept", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
