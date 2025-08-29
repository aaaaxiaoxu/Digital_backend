package com.dj.digitalplatform.model.dto.user;

import com.dj.digitalplatform.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户资质查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQualificationQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 资质审核状态：0 = 待审核/驳回，1 = 通过
     */
    private Integer qualificationStatus;

    /**
     * 教育背景
     */
    private String education;

    /**
     * 工作单位
     */
    private String workplace;

    /**
     * 居住城市
     */
    private String city;

    private static final long serialVersionUID = 1L;
}
