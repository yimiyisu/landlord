package com.yimiyisu.landlord.domain;

import lombok.Data;

@Data
public class LandlordHouseDO {
    /**
     * 业主id
     */
    private String landlordId;

    /**
     * 房屋id
     */
    private String houseId;

    /**
     * 是否为产权人
     */
    private int isOwner;
}
