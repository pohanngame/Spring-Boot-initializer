package com.lab.training.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门实体类（对应数据库 lab_training.department 表）。
 *
 * <p><b>命名约定：</b>Java 里属性用驼峰（createTime），数据库里字段用下划线（create_time）。
 * 我们在 application.yml 里开了 map-underscore-to-camel-case=true，
 * MyBatis 会自动把 create_time 映射成 createTime。
 *
 * <p><b>为什么不直接用数据库表自动生成？</b>因为手写一遍你才能记住每个字段的含义，
 * 也才知道 Controller 返回的 JSON 里到底有哪些字段。
 *
 * <p><b>@Data 是 Lombok 注解</b>，编译时会自动生成 getter/setter/toString/equals/hashCode。
 * 如果 IDEA 里 getter 找不到、运行报 "找不到符号"，说明 Lombok 插件没装或注解处理没开，
 * 处理方法见 README 的常见报错章节。
 */
@Data
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键，数据库自增 */
    private Long id;

    /** 部门名称，例如：研发部 */
    private String name;

    /** 部门编码，业务上要求唯一，例如：DEV、HR、FIN */
    private String code;

    /** 部门负责人姓名 */
    private String leader;

    /** 联系电话 */
    private String phone;

    /** 状态：1=启用，0=停用。用 Integer 而不是 boolean，方便以后扩展更多状态 */
    private Integer status;

    /**
     * 创建时间。
     * 数据库里由 DEFAULT CURRENT_TIMESTAMP 自动填充，Java 侧只读不写。
     * @JsonFormat 用来控制返回给前端的格式，不加的话 LocalDateTime 会输出成
     * "2026-10-30T10:00:00" 这种带 T 的格式，不方便展示。
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间，由数据库 ON UPDATE CURRENT_TIMESTAMP 自动维护 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
