package com.yimiyisu.landlord.domain;

/*
* 扫码结果的错误码
* */
public enum ErrorCode {
    // 核心错误码定义
    VISITOR_ALREADY_ENTERED(100,
            "访客已进入，访客码无效"),

    QR_CODE_SCAN_FAILED(101,
            "扫码失败，请重新生成二维码"),

    QR_CODE_EXPIRED(102,
            "二维码不在使用有效期"),

    QR_CODE_INVALID(103,
            "二维码已失效，请重新生成"),

    UNEXPECTED_ERROR(999,
            "系统异常，请稍后重试");

    // 枚举属性
    private final int code;
    private final String message;

    // 枚举构造函数
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    // 通过错误码查找枚举
    public static String fromCode(int code) {
        for (ErrorCode error : values()) {
            if (error.code == code) {
                return error.message;
            }
        }
        return UNEXPECTED_ERROR.message; // 默认返回未知错误
    }
}
