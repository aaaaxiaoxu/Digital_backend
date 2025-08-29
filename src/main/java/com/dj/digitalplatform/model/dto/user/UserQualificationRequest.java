package com.dj.digitalplatform.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户资质信息更新请求
 */
@Data
public class UserQualificationRequest implements Serializable {

    /**
     * 教育背景：undergraduate, master, doctorate
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

    private static final long serialVersionUID = 1L;
}
