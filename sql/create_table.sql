-- Create database
create database if not exists dj;
use dj;

-- Drop tables
DROP TABLE IF EXISTS user;


-- User table
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment 'Account',
    userPassword varchar(512)                           not null comment 'Password',
    email        varchar(256)                           null comment 'Email',
    userName     varchar(256)                           null comment 'Username',
    userAvatar   varchar(1024)                          null comment 'Avatar',
    userProfile  varchar(512)                           null comment 'Profile',
    userRole     varchar(256) default 'user'            not null comment 'Role: user/admin',
    editTime     datetime     default CURRENT_TIMESTAMP not null comment 'Edit time',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment 'Create time',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'Update time',
    isDelete     tinyint      default 0                 not null comment 'Is deleted',
    phoneNumber  varchar(256)                           null comment 'Phone Number',
    qualificationFile  varchar(1024)                          null comment 'Qualification file (image)',
    qualificationStatus tinyint default 0                     null comment 'Qualification review status: 0 = pending/rejected, 1 = approved',
    education          varchar(50)                            null comment 'Education: undergraduate, master, doctorate',
    workplace          varchar(512)                           null comment 'Workplace/Study institution (e.g., company, university)',
    city               varchar(256)                           null comment 'City of residence',
    UNIQUE KEY uk_userAccount (userAccount),
    UNIQUE KEY uk_email (email),
    INDEX idx_userName (userName)
) comment 'User' collate = utf8mb4_unicode_ci;



CREATE TABLE IF NOT EXISTS institution (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '机构ID',
    institutionName VARCHAR(512) NOT NULL COMMENT '机构名称',
    institutionNameEnglish varchar(512) NOT NULL COMMENT '机构英文名称',
    institutionType VARCHAR(256) NOT NULL COMMENT '机构类型 (例如：大学，研究机构)',
    countryRegion VARCHAR(256) NOT NULL COMMENT '国家/地区 (例如：中国，美国)',
    city VARCHAR(256) NOT NULL COMMENT '城市 (例如：北京，上海)',
    geographicalCoordinates VARCHAR(256) NOT NULL COMMENT '地理坐标 (例如：经纬度)',
    institutionWebsite VARCHAR(1024) NULL COMMENT '机构网站',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除 (0: 否, 1: 是)'
) COMMENT='机构信息表' COLLATE = utf8mb4_unicode_ci;



CREATE TABLE IF NOT EXISTS program (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '专业ID',
    programName VARCHAR(512) NOT NULL COMMENT '专业名称',
    programNameEnglish VARCHAR(512) NULL COMMENT '专业英文名称',
    degreeType VARCHAR(256) NOT NULL COMMENT '学位类型 (例如：本科、硕士、博士)',
    programDuration VARCHAR(256) NOT NULL COMMENT '学制 (例如：4年、2年)',
    languageOfInstruction VARCHAR(256) NOT NULL COMMENT '授课语言 (例如：中文、英文)',
    deliveryMode VARCHAR(256) NOT NULL COMMENT '课程模式 (例如：全日制、兼职、在线、混合)',
    programOverview TEXT NULL COMMENT '专业概述',
    coreCourses TEXT NULL COMMENT '核心课程',
    researchFocus TEXT NULL COMMENT '研究方向',
    programHighlights TEXT NULL COMMENT '专业亮点',
    facultyInformation TEXT NULL COMMENT '师资力量',
    projectExamples TEXT NULL COMMENT '项目案例',
    careerProspects TEXT NULL COMMENT '就业方向',
    partnerInstitutions TEXT NULL COMMENT '合作机构',
    accreditationStatus VARCHAR(256) NULL COMMENT '认证情况',
    institution_id BIGINT NOT NULL COMMENT '所属机构ID',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除 (0: 否, 1: 是)',
    FOREIGN KEY (institution_id) REFERENCES institution(id) ON DELETE CASCADE
) COMMENT='专业信息表' COLLATE = utf8mb4_unicode_ci;
