package com.xuan.common;

import lombok.Data;

/**
 * 分页请求
 *
 * @author xuan
 */
@Data
public class PageRequest {

	/**
	 * 当前页号（默认1）
	 */
	private long current = 1;

	/**
	 * 页面大小（默认10）
	 */
	private long pageSize = 10;

	/**
	 * 排序字段
	 */
	private String sortField;

	/**
	 * 升序 true/false（默认升序）
	 */
	private boolean ascend = true;

	/**
	 * 是否查询总量total 默认位true
	 */
	private boolean needTotal = true;
}
