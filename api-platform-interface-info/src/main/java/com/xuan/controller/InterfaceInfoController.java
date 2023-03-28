package com.xuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuan.client.UserClient;
import com.xuan.dto.InterfaceInfoAddDTO;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.common.*;
import com.xuan.exception.BusinessException;

import com.xuan.model.enums.InterfaceInfoStatusEnum;
import com.xuan.model.vo.UserVO;
import com.xuan.service.InterfaceInfoService;
import com.xuan.dto.InterfaceInfoQueryDTO;
import com.xuan.dto.InterfaceInfoUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * API信息接口
 *
 * @author xuan
 */

@Slf4j
@RestController
@RequestMapping("/interface/info")
public class InterfaceInfoController {

	@Resource
	private InterfaceInfoService interfaceInfoService;

	@Resource
	private UserClient userClient;


	// 测试
	@GetMapping(value = "/test1")
	public Result<UserVO> test() {
		return userClient.getUserById(1L);
	}

	@GetMapping(value = "/test2")
	public Result<UserVO> test2(HttpServletRequest request) {
		return userClient.getCurrentUser();
	}


	// region 增删改查

	/**
	 * 创建
	 *
	 * @param interfaceInfoAddDTO
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public Result<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddDTO interfaceInfoAddDTO, HttpServletRequest request) {
		if (interfaceInfoAddDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoAddDTO, interfaceInfo);
		// 校验
		interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
		// 设置当前用户id
		Result<UserVO> userVOResult = userClient.getCurrentUser();
		UserVO userVO = userVOResult.getData();
		System.out.println("userVO = " + userVO);
		interfaceInfo.setUserId(userVO.getId());
		boolean result = interfaceInfoService.save(interfaceInfo);
		if (!result) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR);
		}
		long newInterfaceInfoId = interfaceInfo.getId();
		return Result.success(newInterfaceInfoId);
	}

	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public Result<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Result<UserVO> userVOResult = userClient.getCurrentUser();
		UserVO userVO = userVOResult.getData();
		long id = deleteRequest.getId();
		// 判断是否存在
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可删除
		if (!oldInterfaceInfo.getUserId().equals(userVO.getId()) && !userClient.currentUserIsAdmin()) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = interfaceInfoService.removeById(id);
		return Result.success(b);
	}

	/**
	 * 更新
	 *
	 * @param interfaceInfoUpdateDTO
	 * @param request
	 * @return
	 */
	@PostMapping("/update")
	public Result<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateDTO interfaceInfoUpdateDTO,
	                                           HttpServletRequest request) {
		if (interfaceInfoUpdateDTO == null || interfaceInfoUpdateDTO.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoUpdateDTO, interfaceInfo);
		// 参数校验
		interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
		Result<UserVO> userVOResult = userClient.getCurrentUser();
		UserVO userVO = userVOResult.getData();
		System.out.println(userVO);
		long id = interfaceInfoUpdateDTO.getId();
		// 判断是否存在
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可修改
		if (!oldInterfaceInfo.getUserId().equals(userVO.getId()) && !userClient.currentUserIsAdmin()) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = interfaceInfoService.updateById(interfaceInfo);
		return Result.success(result);
	}

	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/{id}")
	public Result<InterfaceInfo> getInterfaceInfoById(@PathVariable(value = "id") long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
		return Result.success(interfaceInfo);
	}

	/**
	 * 获取列表（仅管理员可使用）
	 *
	 * @param interfaceInfoQueryDTO
	 * @return
	 */
	@GetMapping("/list")
	public Result<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryDTO interfaceInfoQueryDTO, HttpServletRequest request) {
		boolean currentUserIsAdmin = userClient.currentUserIsAdmin();
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
		if (interfaceInfoQueryDTO != null) {
			BeanUtils.copyProperties(interfaceInfoQueryDTO, interfaceInfoQuery);
		}
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
		List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
		return Result.success(interfaceInfoList);
	}

	/**
	 * 分页获取列表
	 *
	 * @param interfaceInfoQueryDTO
	 * @param request
	 * @return
	 */
	@GetMapping("/list/page")
	public Result<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryDTO interfaceInfoQueryDTO, HttpServletRequest request) {
		if (interfaceInfoQueryDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoQueryDTO, interfaceInfoQuery);
		long current = interfaceInfoQueryDTO.getCurrent();
		long size = interfaceInfoQueryDTO.getPageSize();
		String sortField = interfaceInfoQueryDTO.getSortField();
		boolean ascend = interfaceInfoQueryDTO.isAscend();
		String description = interfaceInfoQuery.getDescription();
		// description 需支持模糊搜索
		interfaceInfoQuery.setDescription(null);
		// 限制爬虫
		if (size > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
		queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
		queryWrapper.orderBy(StringUtils.isNotBlank(sortField), ascend, sortField);
		Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
		return Result.success(interfaceInfoPage);
	}

	/**
	 * 上线接口
	 *
	 * @param idRequest 携带id
	 * @return 是否上线成功
	 */
	@PostMapping("/online")
	public Result<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
		boolean currentUserIsAdmin = userClient.currentUserIsAdmin();
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		if (idRequest == null || idRequest.getId() < 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 判断接口是否存在
		long id = idRequest.getId();
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 判断接口是否能使用
		// TODO 根据测试地址来调用
		// 这里我先用固定的方法进行测试，后面来改
		// com.xuan.model.User user = new com.xuan.model.User();
		// user.setName("MARS");
		// String name = xuanApiClient.getNameByPostWithJson(user);
		// if (StrUtil.isBlank(name)) {
		// 	throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
		// }
		// 更新数据库
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setId(id);
		interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
		boolean isSuccessful = interfaceInfoService.updateById(interfaceInfo);
		return Result.success(isSuccessful);
	}

	/**
	 * 下线接口
	 *
	 * @param idRequest 携带id
	 * @return 是否下线成功
	 */
	@PostMapping("/offline")
	public Result<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
		boolean currentUserIsAdmin = userClient.currentUserIsAdmin();
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		if (idRequest == null || idRequest.getId() < 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 判断接口是否存在
		long id = idRequest.getId();
		InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
		if (oldInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 更新数据库
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setId(id);
		interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
		boolean isSuccessful = interfaceInfoService.updateById(interfaceInfo);
		return Result.success(isSuccessful);
	}

	// /**
	//  * 在线调用接口
	//  *
	//  * @param invokeInterfaceRequest 携带id、请求参数
	//  * @return data
	//  */
	// @PostMapping("/invoke")
	// public Result<Object> invokeInterface(@RequestBody InvokeInterfaceRequest invokeInterfaceRequest, HttpServletRequest request) throws UnsupportedEncodingException {
	// 	if (invokeInterfaceRequest == null || invokeInterfaceRequest.getId() < 0) {
	// 		throw new BusinessException(ErrorCode.PARAMS_ERROR);
	// 	}
	// 	// 判断接口是否存在
	// 	long id = invokeInterfaceRequest.getId();
	// 	InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
	// 	if (interfaceInfo == null) {
	// 		throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
	// 	}
	// 	if (interfaceInfo.getStatus() != InterfaceInfoStatusEnum.ONLINE.getValue()) {
	// 		throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口未上线");
	// 	}
	// 	// 得到当前用户
	// 	User loginUser = userService.getLoginUser(request);
	// 	String accessKey = loginUser.getAccessKey();
	// 	String secretKey = loginUser.getSecretKey();
	// 	XuanApiClient client = new XuanApiClient(accessKey, secretKey);
	// 	// 先写死请求
	// 	String userRequestParams = invokeInterfaceRequest.getRequestParams();
	// 	com.xuan.model.User user = JSONUtil.toBean(userRequestParams, com.xuan.model.User.class);
	// 	String result = client.getNameByPostWithJson(user);
	// 	return ResultUtils.success(result);
	// }

}
