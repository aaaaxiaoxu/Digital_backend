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
