package com.yimiyisu.landlord.domain;

import lombok.Data;

@Data
public class PetDO {
    private String id;

    /**
     * 房屋id
     */
    private String houseId;

    /**
     * 登记时间
     */
    private long createGmt;

    /**
     * 宠物名
     */
    private String title;

    /**
     * 种类
     */
    private int species;

    /**
     * 备注
     */
    private String note;
}
