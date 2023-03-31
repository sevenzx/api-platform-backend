package com.xuan.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: xuan
 * @since: 2023/3/30
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayVO implements Serializable {

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

	private static final long serialVersionUID = -6930301560356589130L;
}
