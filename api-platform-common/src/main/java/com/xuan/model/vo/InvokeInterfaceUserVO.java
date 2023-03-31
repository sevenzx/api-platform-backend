package com.xuan.model.vo;

import lombok.Data;

/**
 * 调用API时 需要的user信息
 *
 * @author: xuan
 * @since: 2023/3/30
 */

@Data
public class InvokeInterfaceUserVO {
	/**
	 * id
	 */
	private Long id;

	/**
	 * secret
	 */
	private String userSecret;

}
