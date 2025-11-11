package com.yimiyisu.landlord.domain;

public enum QrType {
    VISITOR("fk"),
    PATROL("patrol"),
    WATER("water");

    private final String code;

    QrType(String code) {
        this.code = code;
    }

    public static String getCode(QrType qrType) {
        return qrType.code;
    }

    public static QrType fromCode(String code) {
        for (QrType method : values()) {
            if (method.code.equals(code)) {
                return method;
            }
        }
        throw new IllegalArgumentException("无效的二维码类型: " + code);
    }
}

