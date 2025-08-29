package com.dj.digitalplatform.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户资质信息视图对象
 */
@Data
public class UserQualificationVO implements Serializable {

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
     * 邮箱
     */
    private String email;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 资质文件URL
     */
    private String qualificationFile;

    /**
     * 资质审核状态：0 = 待审核/驳回，1 = 通过
     */
    private Integer qualificationStatus;

    /**
     * 教育背景
     */
    private String education;

    /**
     * 工作单位/学习机构
     */
    private String workplace;

    /**
     * 居住城市
     */
    private String city;

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
