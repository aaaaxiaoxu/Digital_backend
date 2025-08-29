package com.dj.digitalplatform.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    private String userAccount;


    private String userPassword;


    private String email;


    private String userName;


    private String userAvatar;

    private String userProfile;


    private String userRole;


    private Date editTime;


    private Date createTime;


    private Date updateTime;


    @TableLogic
    private Integer isDelete;

    private String phoneNumber;

    private String qualificationFile;

    private Integer qualificationStatus;

    private String education;

    private String workplace;

    private String city;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
