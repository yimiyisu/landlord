package com.yimiyisu.landlord.controller;

import java.util.List;

import com.yimiyisu.landlord.domain.CarInfoDO;
import com.yimiyisu.landlord.domain.CommunityManagerDO;
import com.yimiyisu.landlord.domain.ErrorCode;
import com.yimiyisu.landlord.domain.QrType;
import com.yimiyisu.landlord.domain.ScanQrCodeDO;
import com.yimiyisu.landlord.events.model.PayCallbackEventModel;
import com.yimiyisu.landlord.kit.WeixinKit;
import com.yimiyisu.landlord.service.VisitorService;
import com.zen.ZenController;
import com.zen.ZenData;
import com.zen.ZenEngine;
import com.zen.ZenResult;
import com.zen.annotation.AccessRole;
import com.zen.annotation.Inject;
import com.zen.annotation.MethodType;
import com.zen.domain.DecryptResult;
import com.zen.domain.ZenUser;
import com.zen.enums.UserBasicTag;
import com.zen.enums.ZenException;
import com.zen.enums.ZenMethod;
import com.zen.enums.ZenRole;
import com.zen.kit.ConfigKit;
import com.zen.kit.DateKit;
import com.zen.kit.EventKit;
import com.zen.kit.PatternKit;
import com.zen.kit.StringKit;
import com.zen.kit.UploadKit;
import com.zen.kit.UserKit;

@AccessRole(ZenRole.ANONYMITY)
public class Home extends ZenController {

    @Inject
    private ZenEngine zenEngine;
    @Inject
    private VisitorService visitorService;

    // 业务支付回调 异步修改订单信息
    @MethodType(ZenMethod.ALL)
    public ZenResult payCallback(ZenData data) {
        String payId = data.get("id"); // 支付对象id
        PayCallbackEventModel payCallbackEventModel = new PayCallbackEventModel();
        payCallbackEventModel.setPayId(payId);
        EventKit.trigger(payCallbackEventModel);
        return ZenResult.success();
    }

    // 微信小程序登录校验
    public ZenResult wxMiniAppCheck(ZenData data) {
        ZenUser user = data.getUser();
        // 用户身份信息有效，直接返回
        if (user != null)
            return ZenResult.success();
        String openId = WeixinKit.getOpenIdByCode(data.get("code"));
        if (openId == null)
            return ZenResult.fail("获取用户信息异常");
        // 这里判断用户是否入口
        ZenUser zenUser = UserKit.getByOpenId(openId, UserBasicTag.WECHAT.value());
        if (zenUser == null) {
            zenUser = new ZenUser();
            // 静默授权，无需获取头像与昵称
            // String path = "/f01/avator/" + StringKit.objectId() + ".jpg";
            // 保存用户头像
            // String avator = UploadKit.image(data.get("avatarUrl"), path);
            // user.setAvatar(avator);
            zenUser.setOpenId(openId);
            zenUser.setNick("微信用户");
            zenUser.addTag(UserBasicTag.WECHAT);
            String token = UserKit.insert(zenUser);
            return ZenResult.success().setData(token);
        }
        String token = UserKit.createToken(zenUser.getId());
        return ZenResult.success().setData(token);
    }

    /*
     * 获取微信公众号code
     */
    public ZenResult openPlatform(ZenData data) {
        if (data.getUid() != null)
            return ZenResult.success();
        String code = data.get("code");
        String openId = WeixinKit.wxOpenPlatformOpenId(code);
        if (openId == null)
            return ZenResult.fail(ZenException.NO_LOGIN);
        // 判断用户是否存在
        ZenUser zenUser = UserKit.getByOpenId(openId, UserBasicTag.WECHAT.value());
        if (zenUser == null) {
            String uid = StringKit.objectId();
            zenUser = new ZenUser();
            zenUser.setOpenId(openId);
            zenUser.setNick("微信用户");
            zenUser.addTag(UserBasicTag.WECHAT);
            zenUser.setId(uid);

            String token = UserKit.insert(zenUser);
            return ZenResult.success().setData(token);
        }
        String token = UserKit.createToken(zenUser.getId());
        return ZenResult.success().setData(token);
    }

    /*
     * 返回非敏感信息的系统配置，主要供前端使用
     */
    public ZenResult info() {
        return ZenResult.success()
                .put("wxOpenId", ConfigKit.get("wxOpenId")) // 公众号的应用ID
                .put("cdnDomain", ConfigKit.get("cdnDomain")); // CDN域名配置
    }

    public ZenResult wxFile(ZenData data) {
        if (data.isEmpty("serverId"))
            return ZenResult.fail("缺少媒体ID");
        String token = WeixinKit.wxSubcribeToken();
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?media_id=" + data.get("serverId") + "&access_token="
                + token;
        String filePath = "/f01/w" + StringKit.objectId() + ".jpg";
        String path = UploadKit.image(url, filePath);
        return ZenResult.success().setData(path);
    }

    /*
     * 微信服务号js授权
     */
    public ZenResult wxJsAuth(ZenData data) {
        ZenResult result = ZenResult.success();
        String url = data.get("url");
        String jsapiTicket = WeixinKit.wxJsTicket();
        String nonceStr = StringKit.rand(16);
        long timestamp = DateKit.now();

        String originSt = "jsapi_ticket=" + jsapiTicket +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp +
                "&url=" + url;

        result.put("appId", ConfigKit.get("wxSubcribeId"));
        result.put("timestamp", timestamp);
        result.put("nonceStr", nonceStr);
        result.put("signature", StringKit.SHA1(originSt));
        return result;
    }

    /*
     * 小程序授权绑定手机号
     */
    public ZenResult wxBindPhone(ZenData data) {
        ZenUser user = data.getUser();
        if (user.getMobile() != null)
            return ZenResult.fail("您已绑定号码");
        if (data.get("code") == null)
            return ZenResult.fail("手机号获取失败");
        String code = data.get("code");
        String mobile = WeixinKit.wxGetUserPhone(code);
        if (!PatternKit.isMobile(mobile))
            return ZenResult.fail("手机号获取失败");
        boolean hasSuccess = UserKit.updateMobile(user.getId(), mobile);
        if (hasSuccess)
            return ZenResult.success();
        else
            return ZenResult.fail("手机号已被绑定");
    }

    /*
     * 根据车牌查询车辆信息
     */
    public ZenResult getPlate(ZenData data) {
        ZenResult carResult = zenEngine.execute("get/car_byplate",
                ZenData.create(data).put("title", data.get("plate")));
        CarInfoDO carInfo;
        if (!carResult.isEmpty()) {
            carInfo = carResult.asEntity(CarInfoDO.class);
            carInfo.setNumber(carInfo.getTitle());
            carInfo.setCarType("小区车辆");
            return ZenResult.success().setData(carInfo);
        }
        carResult = zenEngine.execute("get/temp_car", ZenData.create(data).put("number", data.get("plate")));
        if (!carResult.isEmpty()) {
            carInfo = carResult.asEntity(CarInfoDO.class);
            carInfo.setCarType("临时车");
            return ZenResult.success().setData(carInfo);
        }
        return ZenResult.success();
    }

    /*
     * 校验保安身份
     */
    public ZenResult getSecurity(ZenData data) {
        ZenResult result = zenEngine.execute("list/community_managerByUid", data);
        if (result.isEmpty()) {
            return ZenResult.success().setData(0);
        }
        List<CommunityManagerDO> list = result.asList(CommunityManagerDO.class);
        for (CommunityManagerDO communityManagerDO : list) {
            if (communityManagerDO.getStatus() == 1) {
                return ZenResult.success().setData(communityManagerDO);
            }
        }
        return ZenResult.success().setData("disable");
    }

    /*
     * 添加运营人员
     */
    public ZenResult putCommunityManager(ZenData data) {
        DecryptResult id = StringKit.decrypt(data.get("code"));
        System.out.println(id.getValue());
        ZenResult res = zenEngine.execute("get/community_manager_security", data.put("id", id.getValue()));
        System.out.println(res);
        if (!res.get("title").equals(data.get("title")) || !res.get("telephone").equals(data.get("telephone"))) {
            return ZenResult.success().setData("fail");
        }
        ZenUser zenUser = UserKit.get(data.getUid());
        zenUser.setNick(data.get("title"));
        zenUser.setMobile(data.get("telephone"));
        UserKit.update(zenUser);
        zenEngine.execute("patch/community_manager", data);
        return ZenResult.success().setData(res).put("uid", data.getUid());
    }

    /*
     * 查询运营人员状态
     */
    public ZenResult getManagerStatus(ZenData data) {
        DecryptResult id = StringKit.decrypt(data.get("code"));
        ZenResult res = zenEngine.execute("get/community_manager_security", data.put("id", id.getValue()));
        if (res.isEmpty()) {
            return ZenResult.success().setData("expired");
        }
        if (res.getInt("status") == 0 || res.getInt("status") == 1) {
            return ZenResult.success().setData("existing");
        }
        if (DateKit.now() - id.getTime() > 600) {
            return ZenResult.success().setData("expired");
        }
        return ZenResult.success().setData("acc");
    }

    /*
     * 扫码
     */
    public ZenResult scanQrCode(ZenData data) {
        String code = data.get("code");
        DecryptResult decrypt = StringKit.decrypt(code);
        String value = decrypt.getValue();
        // 获取二维码解密明文
        String[] strings = value.split("_");
        ScanQrCodeDO scanQrCodeDO = new ScanQrCodeDO();
        scanQrCodeDO.setType(strings[0]);
        scanQrCodeDO.setId(strings[1]);
        // 获取生成二维码的时间戳
        scanQrCodeDO.setTime(decrypt.getTime());
        // 获取枚举
        QrType qrType = QrType.fromCode(scanQrCodeDO.getType());
        int typeCode = 0;
        // 判断二维码类型
        switch (qrType) {
            case VISITOR:
                typeCode = visitorService.scanQrCode(scanQrCodeDO);
                if (typeCode == 200)
                    zenEngine.execute("patch/visitor_enter", ZenData.create(data).put("id", scanQrCodeDO.getId()));
                break;
            case PATROL, WATER:
                typeCode = 200;
                break;
            default:
                System.out.println("未知二维码类型" + qrType);
                return ZenResult.fail("二维码异常");
        }
        if (typeCode != 200)
            return ZenResult.fail(ErrorCode.fromCode(typeCode));
        else
            return ZenResult.success().setData(scanQrCodeDO);
    }

    /**
     * 体验环境移动端登录
     */
    public ZenResult trialLogin(ZenData data) {
        if (ConfigKit.get("trialUid") != null) {
            String token = UserKit.createToken(ConfigKit.get("trialUid"));
            return ZenResult.success().setData(token);
        }
        return ZenResult.success();
    }
}
