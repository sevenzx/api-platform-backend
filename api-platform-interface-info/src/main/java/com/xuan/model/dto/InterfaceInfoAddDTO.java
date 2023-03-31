package com.xuan.model.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 创建请求
 *
 * @author xuan
 */
@Data
public class InterfaceInfoAddDTO implements Serializable {

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 接口地址
	 */
	private String url;

	/**
	 * 请求头
	 */
	private String requestHeader;

	/**
	 * 请求参数
	 */
	private String requestParams;

	/**
	 * 响应头
	 */
	private String responseHeader;

	/**
	 * 请求类型
	 */
	private String method;

}