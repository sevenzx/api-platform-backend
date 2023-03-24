package com.xuan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
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
	 * 性别 0-保密 1-男 2-女
	 */
	private Integer gender;

	/**
	 * 用户角色
	 */
	private String userRole;

	/**
	 * 密码
	 */
	private String userPassword;

	/**
	 * key
	 */
	private String userKey;

	/**
	 * secret
	 */
	private String userSecret;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 是否删除
	 */
	@TableLogic
	private Integer isDelete;

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
}