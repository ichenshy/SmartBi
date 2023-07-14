# 建表脚本

-- 创建库
create database if not exists my_bi;

-- 切换库
use my_bi;
CREATE TABLE IF NOT EXISTS chart_null
(
    日期   varchar(128),
    人数   varchar(128),
    机器人 varchar(128)
);
-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
#     TODO 待添加
    gender        tinyint                            null comment '性别',
    phone         varchar(128)                       null comment '电话',
    email         varchar(255)                       null comment '邮箱',
    user_status   int      default 0                 not null comment '用户状态',
    planet_code   varchar(20)                        null comment '星球id',
    tags          varchar(1024)                      null comment '标签列表',
    profile       varchar(521)                       null comment '个人简介'
) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表表
create table if not exists chart
(
    id           bigint auto_increment comment 'id' primary key,
    goal				 text  null comment '分析目标',
    chartData    text  null comment '图表数据',
    chartType	   varchar(128) null comment '图表类型',
    name	   varchar(128) null comment '图表名称',
    genChart		 text	 null comment '生成的图表数据',
    genResult		 text	 null comment '生成的分析结论',
    userId       bigint null comment '创建用户 id',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;
