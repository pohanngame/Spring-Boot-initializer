-- ============================================================================
--  数据库初始化脚本
--
--  使用方法（三选一）：
--    1. 命令行：mysql -u root -p < sql/init.sql
--    2. Navicat / DataGrip：新建查询窗口，粘贴全部内容后执行
--    3. IDEA 右侧 Database 面板 → 打开控制台 → 粘贴执行
--
--  ⚠️ 脚本末尾的 DROP TABLE 会删掉已有数据，重复执行前请确认不需要旧数据。
-- ============================================================================

-- 1. 创建数据库（字符集用 utf8mb4，才能存 emoji 和生僻字）
CREATE DATABASE IF NOT EXISTS `lab_training`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `lab_training`;

-- 2. 删除旧表，保证脚本可以反复执行（幂等）
DROP TABLE IF EXISTS `department`;

-- 3. 部门表
CREATE TABLE `department`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(50)  NOT NULL COMMENT '部门名称',
    `code`        VARCHAR(20)  NOT NULL COMMENT '部门编码，业务上唯一',
    `leader`      VARCHAR(20)  DEFAULT NULL COMMENT '负责人',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    -- 唯一索引：数据库层面保证 code 不重复。
    -- 这一条非常关键：程序里的"先查再插"在并发下挡不住重复，唯一索引才是最后一道防线。
    UNIQUE KEY `uk_department_code` (`code`),
    KEY `idx_department_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门表';

-- 4. 测试数据（6 条，够你验证查询、修改、删除）
INSERT INTO `department` (`name`, `code`, `leader`, `phone`, `status`)
VALUES ('研发部', 'DEV', '张伟', '13800000001', 1),
       ('产品部', 'PRD', '李静', '13800000002', 1),
       ('人力资源部', 'HR', '王芳', '13800000003', 1),
       ('财务部', 'FIN', '赵敏', '13800000004', 1),
       ('市场部', 'MKT', '刘强', '13800000005', 0),
       ('运维部', 'OPS', '陈晨', '13800000006', 1);

-- 5. 验证：执行完应该看到 6 行数据
SELECT id, name, code, leader, status, create_time FROM `department` ORDER BY id;


-- ============================================================================
--  【阶段二留给你】—— Company / Employee 建表脚本写在这里
--
--  要求（自己动手设计，不要问标准答案，设计思路见 README 阶段二）：
--    1. 选一个方向：Company（公司）或 Employee（员工），也可以两个都做；
--    2. 必须和 department 表建立关联（外键或逻辑外键二选一，但要能解释为什么）；
--    3. 至少包含 6 个业务字段，必须有唯一约束和 NOT NULL 约束；
--    4. 时间字段沿用本脚本的风格（DEFAULT CURRENT_TIMESTAMP）；
--    5. 至少插入 5 条测试数据，并且要能通过 SQL 关联查询出
--       "某部门下有哪些员工/公司"。
--
--  提示：先在本文件里把建表语句写好并执行成功，再去写 Java 代码。
--        顺序反了会非常痛苦——表都没建好，Mapper 怎么写都是错的。
-- ============================================================================
