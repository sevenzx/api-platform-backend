package com.xuan.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 翻页数据
 *
 * @author: xuan
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T>{
	/**
	 * 翻页数据
	 */
	private List<T> records;

	/**
	 * 当前页
	 */
	private long current;

	/**
	 * 当前页数据量
	 */
	private long pageSize;

	/**
	 * 总共数据量
	 */
	private long total;
}
