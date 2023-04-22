package com.xuan.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户查询请求
 *
 * @author xuan
 */
@Data
public class UserQueryDTO implements Serializable {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 用户昵称
	 */
	private String username;

	/**
	 * 账号
	 */
	private String userAccount;

	/**
	 * 用户头像
	 */
	private String userAvatar;

	/**
	 * 性别
	 */
	private Integer gender;

	/**
	 * 用户角色: user, admin
	 */
	private String userRole;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	private static final long serialVersionUID = 1L;
}