package com.xuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.common.FuzzyQueryRequest;
import com.xuan.common.PageRequest;
import com.xuan.model.entity.User;
import com.xuan.model.vo.PageVO;
import com.xuan.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: xuan
 * @since: 2023/2/10
 */

public interface UserService extends IService<User> {
	/**
	 * 用户注册
	 *
	 * @param userAccount   用户账户
	 * @param userPassword  用户密码
	 * @param checkPassword 验证密码
	 * @param request       请求
	 * @return 数据库id
	 */
	long userRegister(String userAccount, String userPassword, String checkPassword, HttpServletRequest request);

	/**
	 * 用户登录
	 *
	 * @param userAccount  用户账户
	 * @param userPassword 用户密码
	 * @param request      请求
	 * @return 脱敏后的用户信息
	 */
	UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

	/**
	 * 当前用户
	 *
	 * @param request HttpServletRequest
	 * @return UserVO
	 */
	UserVO currentUser(HttpServletRequest request);

	/**
	 * 用户注销
	 *
	 * @param request 请求
	 * @return boolean
	 */
	boolean userLogout(HttpServletRequest request);

	/**
	 * 分页查询
	 *
	 * @param pageRequest 分页查询请求
	 * @return PageVO<UserVO>
	 */
	PageVO<UserVO> listUserByPage(PageRequest pageRequest);

	/**
	 * 模糊查询
	 *
	 * @param fuzzyQueryRequest 模糊查询请求
	 * @return PageVO<UserVO>
	 */
	PageVO<UserVO> listUserByFuzzy(FuzzyQueryRequest fuzzyQueryRequest);

	/**
	 * 根据id查询
	 *
	 * @param ids id列表
	 * @return Map<Long, UserVO>
	 */
	Map<Long, UserVO> listUserByIds(List<Long> ids);
}
