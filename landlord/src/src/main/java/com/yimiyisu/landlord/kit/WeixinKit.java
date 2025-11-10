package com.yimiyisu.landlord.kit;

import com.google.gson.JsonObject;
import com.zen.kit.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WeixinKit {
    private final static String TOKEN_CACHE_KEY = "wx-miniapp-token";
    private final static String TOKEN_OPEN_CACHE_KEY = "wx-app-open-token";
    private final static String JSAPI_TICKET = "jsapi_ticket";

    // 微信小程序token生成
    public static String wxMiniAppToken() {
        String token = CacheKit.get(WeixinKit.TOKEN_CACHE_KEY);
        if (StringKit.isNotEmpty(token)) return token;
        HashMap<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", ConfigKit.get("wxMiniAppId"));
        params.put("secret", ConfigKit.get("wxMiniAppSecret"));
        Map<String, Object> result = HttpKit.getAsMap("https://api.weixin.qq.com/cgi-bin/token", params);
        if (result.containsKey("access_token")) {
            token = result.get("access_token").toString();
            int effective = (int) (double) result.get("expires_in") - 60;
            CacheKit.set(WeixinKit.TOKEN_CACHE_KEY, token, (int) (double) (effective / 60));
            return token;
        }
        return null;
    }

    // 微信订阅号，服务号token生成
    public static String wxSubcribeToken() {
        String token = CacheKit.get(WeixinKit.TOKEN_OPEN_CACHE_KEY);
        if (StringKit.isNotEmpty(token)) return token;
        HashMap<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", ConfigKit.get("wxSubcribeId"));
        params.put("secret", ConfigKit.get("wxSubcribeSecret"));
        Map<String, Object> result = HttpKit.getAsMap("https://api.weixin.qq.com/cgi-bin/token", params);
        if (result.containsKey("access_token")) {
            token = result.get("access_token").toString();
            int effective = (int) (double) result.get("expires_in") - 180;
            CacheKit.set(WeixinKit.TOKEN_OPEN_CACHE_KEY, token, (int) (double) (effective / 60));
            return token;
        }
        return null;
    }

    // 根据小程序的code，换取用的openId
    public static String getOpenIdByCode(String code) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("js_code", code);
        params.put("appid", ConfigKit.get("wxMiniAppId"));
        params.put("secret", ConfigKit.get("wxMiniAppSecret"));
        Map<String, Object> result = HttpKit.getAsMap("https://api.weixin.qq.com/sns/jscode2session", params);
        if (result.containsKey("openid")) return result.get("openid").toString();
        WeixinKit.log.error("获取微信openId失败，code：{0}，msg：{1}", result.get("errcode"), result.get("errmsg"));
        return null;
    }

    /**
     * 静默获取微信公众号用户openId，同步返回用户access_token，通过该token可以进一步那用户授权详细信息
     * 官方文档 https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html#0
     */
    public static String wxOpenPlatformOpenId(String code) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appid", ConfigKit.get("wxSubcribeId"));
        params.put("secret", ConfigKit.get("wxSubcribeSecret"));
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        Map<String, Object> result = HttpKit.getAsMap("https://api.weixin.qq.com/sns/oauth2/access_token", params);
        if (result.containsKey("openid")) return result.get("openid").toString();
        return null;
    }

    /*
        通过code 转换用户手机号
    */
    public static String wxGetUserPhone(String code) {
        HashMap<String, Object> params = new HashMap<>();
        String accessToken = WeixinKit.wxMiniAppToken();
        params.put("code", code);
        Map<String, Object> result = HttpKit.postAsMap("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken, params);
        if (result.containsKey("phone_info")) {
            JsonObject phoneInfo = JsonKit.parse(result.get("phone_info"));
            return phoneInfo.get("phoneNumber").getAsString();
        }
        return null;
    }

    public static String wxJsTicket() {
        String ticket = CacheKit.get(WeixinKit.JSAPI_TICKET);
        if (StringKit.isNotEmpty(ticket)) return ticket;
        String accessToken = WeixinKit.wxSubcribeToken();
        Map<String, Object> result = HttpKit.getAsMap("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + accessToken);
        if (result.containsKey("ticket")) {
            ticket = result.get("ticket").toString();
            int effective = (int) (double) result.get("expires_in") - 180;
            CacheKit.set(WeixinKit.JSAPI_TICKET, ticket, (int) (double) (effective / 60));
            return ticket;
        }
        return null;
    }

    public static Object getWxInfo(String url) {
        HashMap<String, Object> info = new HashMap<>();
        Long time = DateKit.now();
        String noncestr = StringKit.shortId();
        String ticket = WeixinKit.wxJsTicket();
        info.put("appId", ConfigKit.get("wxOpenId"));
        info.put("timestamp", time);
        info.put("nonceStr", noncestr);
        info.put("ticket", ticket);
        String str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%d&url=%s", ticket, noncestr, time, url);
        String signature = StringKit.SHA1(str);
        info.put("signature", signature);
        return info;
    }

    /**
     * jsapi包含请求体计算签名
     * 官方文档：https://pay.weixin.qq.com/doc/v3/merchant/4012365336
     * 注：有的签名不含body，可以为null
     *
     * @param requestMethod 请求方式 注：需要大写字母
     * @param url           请求url
     * @param timeStamp     当前时间戳 注：这个timeStamp需要为字符串，不然ios端可能会识别不到该参数
     * @param nonceStr      随机字符串
     * @param body          请求报文主体
     * @return 签名
     */
    public static String getPaySignature(String requestMethod, String url, String timeStamp, String nonceStr, String body) {
        // 获取原签名串
        StringBuilder signContent = new StringBuilder();
        signContent.append(requestMethod).append("\n");
        signContent.append(url.substring(url.indexOf("/v3"))).append("\n");
        signContent.append(timeStamp).append("\n");
        signContent.append(nonceStr).append("\n");
        if (StringUtils.isNotEmpty(body)) signContent.append(body).append("\n");
        else signContent.append("\n");

        // 签名
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            PrivateKey privateKey = WeixinKit.getPrivateKey(); // 商户私钥
            signature.initSign(privateKey);
            signature.update(signContent.toString().getBytes(StandardCharsets.UTF_8));
            byte[] signBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * jsapi调起支付签名
     * 官方文档：https://pay.weixin.qq.com/doc/v3/merchant/4012365339
     *
     * @param appId     服务号id
     * @param timeStamp 时间戳  注：这个timeStamp需要为字符串，不然ios端可能会识别不到该参数
     * @param nonceStr  随机字符串
     * @param prepayId  预支付id 注：构造签名串时，这里的prepay_id需要加前缀，不能直接拼prepay_id
     * @return 签名
     */
    public static String getJsAPISignature(String appId, String timeStamp, String nonceStr, String prepayId) {
        String signatureStr = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + "prepay_id=" + prepayId + "\n";
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            PrivateKey privateKey = WeixinKit.getPrivateKey();
            signature.initSign(privateKey);
            signature.update(signatureStr.getBytes(StandardCharsets.UTF_8));
            byte[] signBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 支付回调签名验签 - 微信支付公钥方式
     * 官方文档：https://pay.weixin.qq.com/doc/v3/merchant/4013053249
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param body      请求报文主体
     * @param serial    微信支付公钥id
     * @return 签名验证结果
     */
    public static boolean payNotifyVerify(String signature, String timestamp, String nonce, String body, String serial) {
        if (!ConfigKit.get("wxpay_v3_platformPubId").equals(serial)) return false; // 验证证书序列号

        // 构建签名字符串
        String message = timestamp + "\n" + nonce + "\n" + body + "\n";

        try {
            // 使用微信支付平台公钥验证签名
            PublicKey wechatPlatformPublicKey = WeixinKit.getPlatformPub();
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initVerify(wechatPlatformPublicKey);
            signer.update(message.getBytes(StandardCharsets.UTF_8));

            // 验证签名
            return signer.verify(Base64.getDecoder().decode(signature));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解密数据（AES-256-GCM）
     * 支付回调数据解密
     */
    public static String decrypt(String associatedData, String nonce, String ciphertext) throws Exception {
        try {
            // 使用APIv3密钥作为密钥
            byte[] key = ConfigKit.get("wxpay_v3_apiKey").getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

            // 配置GCM参数
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));

            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
            if (associatedData != null) cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));

            // 解密数据
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }


    // 从文件加载平台公钥 - 用于支付回调验签
    private static PublicKey getPlatformPub() throws Exception {
        String wxpayV3PlatformPub = IOKit.readToString(ConfigKit.get("wxpay_v3_platformPub"));
        wxpayV3PlatformPub = wxpayV3PlatformPub.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(wxpayV3PlatformPub);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    // 从文件加载商户私钥
    private static PrivateKey getPrivateKey() throws Exception {
        String wxpayV3KeyPath = IOKit.readToString(ConfigKit.get("wxpay_v3_keyPath"));
        String cleanedKey = wxpayV3KeyPath
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
