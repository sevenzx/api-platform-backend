package com.xuan.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 模糊查询请求
 *
 * @author: xuan
 * @since: 2023/4/21
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class FuzzyQueryRequest extends PageRequest {

	/**
	 * 模糊查询字段
	 */
	private List<String> fields;

	/**
	 * 模糊查询关键字
	 */
	private String keyword;

}
