package com.dj.digitalplatform.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户资质审核请求
 */
@Data
public class UserQualificationAuditRequest implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
