package com.dj.digitalplatform.model.dto.institution;

import com.dj.digitalplatform.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 机构查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InstitutionQueryRequest extends PageRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}
