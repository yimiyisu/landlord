package com.yimiyisu.landlord.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Z-熙玉
 * @version 1.0
 * 区域通用类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AreaDo {

    private String id;
    private String title;
    private String parentId;
    private Integer isLeaf;
    private List<AreaDo> children = new ArrayList<>();

}
