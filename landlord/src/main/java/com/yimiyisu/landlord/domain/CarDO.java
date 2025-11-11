package com.yimiyisu.landlord.domain;

import lombok.Data;

@Data
public class CarDO {
    private String id;
    private String houseId;
    private long createGmt;

    /**
     * 品牌型号
     */
    private String model;

    /**
     * 车牌号
     */
    private String title;

    /**
     * 车位id
     */
    private String parkingId;

    /**
     * 收费标准
     */
    private String fee;

    /**
     * 车辆车位状态
     */
    private int status;

    /**
     * 到期时间
     */
    private long endTime;

    /**
     * 车辆区域
     */
    private String areaTitle;

    /**
     * 车位类型
     */
    private String parkingType;
}
