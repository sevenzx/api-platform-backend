package com.xuan.util;

import cn.hutool.core.util.StrUtil;
import com.xuan.common.ErrorCode;
import com.xuan.exception.BusinessException;
import com.xuan.model.enums.UserRoleEnum;
import com.xuan.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

import static com.xuan.constant.CommonConstant.USER_LOGIN_STATE;

/**
 * @author: xuan
 * @since: 2023/4/13
 */
public class UserUtil {

	/**
	 * 获取当前用户信息
	 *
	 * @param request HttpServletRequest
	 * @return UserVO
	 */
	public static UserVO currentUser(HttpServletRequest request) {
		Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
		if (userObj == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// TODO 在返回前看需不需要再次查询数据库 因为信息可能更新 但是主要的id等不会改变
		return (UserVO) userObj;
	}

	/**
	 * 是否有管理员权限
	 *
	 * @param request HttpServletRequest
	 * @return boolean
	 */
	public static boolean hasAdminPermission(HttpServletRequest request) {
		Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
		if (userObj == null) {
			return false;
		}
		UserVO user = (UserVO) userObj;
		return StrUtil.equals(user.getUserRole(), UserRoleEnum.ADMIN.getRole());
	}

}
