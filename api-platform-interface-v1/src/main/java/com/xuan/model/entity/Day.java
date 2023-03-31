package com.xuan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日信息
 * @TableName day
 */
@TableName(value ="day")
@Data
public class Day implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 日期
     */
    private Integer date;

    /**
     * 日期字符串
     */
    private String dateStr;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 当年第几天
     */
    private Integer yearDay;

    /**
     * 当周第几天
     */
    private Integer week;

    /**
     * 是否为周末(0-否 1-是)
     */
    private Integer weekend;

    /**
     * 是否为工作日(0-否 1-是)
     */
    private Integer workday;

    /**
     * 节假日
     */
    private String holiday;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}