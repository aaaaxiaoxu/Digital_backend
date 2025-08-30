package com.dj.digitalplatform.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构视图对象
 */
@Data
public class InstitutionVO implements Serializable {

    /**
     * 机构ID
     */
    private Long id;

    /**
     * 机构名称
     */
    private String institutionName;

    /**
     * 机构英文名称
     */
    private String institutionNameEnglish;

    /**
     * 机构类型 (例如：大学，研究机构)
     */
    private String institutionType;

    /**
     * 国家/地区 (例如：中国，美国)
     */
    private String countryRegion;

    /**
     * 城市 (例如：北京，上海)
     */
    private String city;

    /**
     * 地理坐标 (例如：经纬度)
     */
    private String geographicalCoordinates;

    /**
     * 机构网站
     */
    private String institutionWebsite;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
