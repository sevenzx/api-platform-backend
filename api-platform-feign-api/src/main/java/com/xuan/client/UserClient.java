package com.xuan.client;

import com.xuan.common.Result;
import com.xuan.model.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: xuan
 * @since: 2023/3/28
 */

@FeignClient(value = "api-platform-user", path = "/api/user")
public interface UserClient {

	/**
	 * 当前登录用户
	 *
	 * @return UserVO
	 */
	@GetMapping(value = "/current")
	Result<UserVO> getCurrentUser();

	/**
	 * 是否有管理员权限
	 *
	 * @return boolean
	 */
	@GetMapping(value = "/current/isAdmin")
	boolean currentUserIsAdmin();

	/**
	 * 根据 id 获取用户
	 *
	 * @param id id
	 * @return Result<UserVO>
	 */
	@GetMapping(value = "/get/{id}")
	Result<UserVO> getUserById(@PathVariable("id") Long id);

}