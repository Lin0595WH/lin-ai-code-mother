-- 创建库
CREATE DATABASE IF NOT EXISTS lin_ai_code_mother;

-- 切换库
USE lin_ai_code_mother;

-- 用户表
-- 以下是建表语句

-- 用户表
CREATE TABLE IF NOT EXISTS user
(
    id            bigint AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    user_account  varchar(256)                           NOT NULL COMMENT '账号',
    user_password varchar(512)                           NOT NULL COMMENT '密码',
    user_name     varchar(256)                           NULL COMMENT '用户昵称',
    user_avatar   varchar(1024)                          NULL COMMENT '用户头像',
    user_profile  varchar(512)                           NULL COMMENT '用户简介',
    user_role     varchar(256) DEFAULT 'user'            NOT NULL COMMENT '用户角色：user/admin',
    edit_time     datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '编辑时间',
    create_time   datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time   datetime     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete     tinyint      DEFAULT 0                 NOT NULL COMMENT '是否删除',
    UNIQUE KEY uk_userAccount (user_account),
    INDEX idx_userName (user_name)
) COMMENT '用户' COLLATE = utf8mb4_unicode_ci;


-- 应用表
CREATE TABLE IF NOT EXISTS app
(
    id            bigint AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    app_name      varchar(256)                       NULL COMMENT '应用名称',
    cover         varchar(512)                       NULL COMMENT '应用封面',
    init_prompt   text                               NULL COMMENT '应用初始化的 prompt',
    code_gen_type varchar(64)                        NULL COMMENT '代码生成类型（枚举）',
    deploy_key    varchar(64)                        NULL COMMENT '部署标识',
    deployed_time datetime                           NULL COMMENT '部署时间',
    priority      int      DEFAULT 0                 NOT NULL COMMENT '优先级',
    user_id       bigint                             NOT NULL COMMENT '创建用户id',
    edit_time     datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '编辑时间',
    create_time   datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    update_time   datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete     tinyint  DEFAULT 0                 NOT NULL COMMENT '是否删除',
    UNIQUE KEY uk_deploy_key (deploy_key), -- 确保部署标识唯一
    INDEX idx_app_name (app_name),         -- 提升基于应用名称的查询性能
    INDEX idx_user_id (user_id)            -- 提升基于用户 ID 的查询性能
) COMMENT '应用' COLLATE = utf8mb4_unicode_ci;