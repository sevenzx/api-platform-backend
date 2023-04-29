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
	 * 是否查询总量total 默认为true
	 */
	private boolean needTotal = true;
}
