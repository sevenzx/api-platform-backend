package com.xuan.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author xuan
 */
@Data
public class UserAddDTO implements Serializable {

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
     * 密码
     */
    private String userPassword;

    private static final long serialVersionUID = 1L;
}