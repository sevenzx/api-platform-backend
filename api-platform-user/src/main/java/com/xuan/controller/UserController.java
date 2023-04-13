package com.xuan.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuan.dto.*;
import com.xuan.model.entity.User;
import com.xuan.common.Result;
import com.xuan.common.DeleteRequest;
import com.xuan.common.ErrorCode;
import com.xuan.exception.BusinessException;
import com.xuan.model.enums.UserRoleEnum;
import com.xuan.model.vo.InvokeInterfaceUserVO;
import com.xuan.model.vo.PageVO;
import com.xuan.model.vo.UserVO;
import com.xuan.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.xuan.constant.CommonConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author xuan
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;

	/**
	 * 用户注册
	 *
	 * @param userRegisterDTO 请求参数
	 * @param request         HttpServletRequest
	 * @return 注册用户id
	 */
	@PostMapping("/register")
	public Result<Long> userRegister(@RequestBody UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
		if (userRegisterDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String userAccount = userRegisterDTO.getUserAccount();
		String userPassword = userRegisterDTO.getUserPassword();
		String checkPassword = userRegisterDTO.getCheckPassword();
		if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long userId = userService.userRegister(userAccount, userPassword, checkPassword, request);
		if (userId <= 0) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}
		return Result.success(userId);
	}

	/**
	 * 用户登录
	 *
	 * @param userLoginDTO 请求参数
	 * @param request      HttpServletRequest
	 * @return UserVO
	 */
	@PostMapping("/login")
	public Result<UserVO> userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
		if (userLoginDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String userAccount = userLoginDTO.getUserAccount();
		String userPassword = userLoginDTO.getUserPassword();
		if (StrUtil.hasBlank(userAccount, userPassword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		UserVO userVO = userService.userLogin(userAccount, userPassword, request);
		if (userVO == null) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}
		return Result.success(userVO);
	}

	/**
	 * 当前登录用户
	 *
	 * @param request HttpServletRequest
	 * @return UserVO
	 */
	@GetMapping("/current")
	public Result<UserVO> getCurrentUser(HttpServletRequest request) {
		Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
		if (userObj == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		UserVO userVO = userService.currentUser(request);
		return Result.success(userVO);
	}

	/**
	 * 用户注销
	 *
	 * @param request HttpServletRequest
	 * @return 是否成功
	 */
	@PostMapping("/logout")
	public Result<Boolean> userLogout(HttpServletRequest request) {
		if (request == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean result = userService.userLogout(request);
		return Result.success(result);
	}

	/**
	 * 创建用户
	 *
	 * @param userAddDTO UserAddDTO
	 * @return Result<Long>
	 */
	@PostMapping("/add")
	public Result<Long> addUser(@RequestBody UserAddDTO userAddDTO) {
		if (userAddDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = new User();
		BeanUtils.copyProperties(userAddDTO, user);
		boolean result = userService.save(user);
		if (!result) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR);
		}
		return Result.success(user.getId());
	}

	/**
	 * 删除用户
	 *
	 * @param deleteRequest DeleteRequest
	 * @return Result<Boolean>
	 */
	@PostMapping("/delete")
	public Result<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean b = userService.removeById(deleteRequest.getId());
		return Result.success(b);
	}

	/**
	 * 更新用户
	 *
	 * @param userUpdateDTO UserUpdateDTO
	 * @return Result<Boolean>
	 */
	@PostMapping("/update")
	public Result<Boolean> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
		if (userUpdateDTO == null || userUpdateDTO.getId() == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = new User();
		BeanUtils.copyProperties(userUpdateDTO, user);
		boolean result = userService.updateById(user);
		return Result.success(result);
	}

	/**
	 * 根据 id 获取用户
	 *
	 * @param id id
	 * @return Result<UserVO>
	 */
	@GetMapping("/get/{id}")
	public Result<UserVO> getUserById(@PathVariable("id") Long id) {
		if (id <= 0L) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getById(id);
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		return Result.success(userVO);
	}

	/**
	 * 用户翻页查询
	 *
	 * @param userQueryDTO 查询参数
	 * @param request      HttpServletRequest
	 * @return Page<UserVO>
	 */
	@PostMapping("/list/page")
	public Result<PageVO<UserVO>> listUserByPage(@RequestBody UserQueryDTO userQueryDTO, HttpServletRequest request) {
		if (!hasAdminPermission(request)) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
		if (userQueryDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		PageVO<UserVO> pageVO = userService.listUserByPage(userQueryDTO);
		return Result.success(pageVO);
	}

	@GetMapping("/get/secret")
	public Result<InvokeInterfaceUserVO> getSecretByKey(@RequestParam(value = "userKey") String userKey) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getUserKey, userKey);
		User user = userService.getOne(queryWrapper);
		InvokeInterfaceUserVO invokeInterfaceUserVO = new InvokeInterfaceUserVO();
		if (user != null) {
			invokeInterfaceUserVO.setId(user.getId());
			invokeInterfaceUserVO.setUserSecret(user.getUserSecret());
		}
		return Result.success(invokeInterfaceUserVO);
	}


	/**
	 * 是否有管理员权限
	 *
	 * @param request HttpServletRequest
	 * @return boolean
	 */
	public boolean hasAdminPermission(HttpServletRequest request) {
		Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
		if (userObj == null) {
			return false;
		}
		UserVO user = (UserVO) userObj;
		return StrUtil.equals(user.getUserRole(), UserRoleEnum.ADMIN.getRole());
	}
}
