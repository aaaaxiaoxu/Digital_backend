package com.dj.digitalplatform.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员审核用户资质请求
 */
@Data
public class UserVerifyAuditRequest implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 审核结果: approved/rejected
     */
    private String verifyStatus;

    /**
     * 审核备注
     */
    private String auditNote;

    private static final long serialVersionUID = 1L;
}
