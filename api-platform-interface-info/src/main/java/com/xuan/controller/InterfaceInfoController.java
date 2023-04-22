package com.xuan.controller;

import cn.hutool.core.util.StrUtil;
import com.xuan.model.dto.InterfaceInfoAddDTO;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.common.*;
import com.xuan.exception.BusinessException;

import com.xuan.model.vo.PageVO;
import com.xuan.model.vo.UserVO;
import com.xuan.service.InterfaceInfoService;
import com.xuan.model.dto.InterfaceInfoUpdateDTO;
import com.xuan.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


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

	@GetMapping("/test")
	public Result<UserVO> testUserClient(HttpServletRequest request) {
		Object userLoginState = request.getSession().getAttribute("userLoginState");
		System.out.println("userLoginState = " + userLoginState);
		return Result.success(null);
	}


	/**
	 * 新增
	 *
	 * @param interfaceInfoAddDTO InterfaceInfoAddDTO
	 * @return 数据库主键id
	 */
	@PostMapping("/add")
	public Result<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddDTO interfaceInfoAddDTO, HttpServletRequest request) {
		if (interfaceInfoAddDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long id = interfaceInfoService.addInterfaceInfo(interfaceInfoAddDTO, request);
		return Result.success(id);
	}

	/**
	 * 删除
	 *
	 * @param deleteRequest DeleteRequest
	 * @return 是否成功
	 */
	@PostMapping("/delete")
	public Result<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.deleteInterfaceInfo(deleteRequest, request);
		return Result.success(isSuccessful);
	}

	/**
	 * 更新
	 *
	 * @param interfaceInfoUpdateDTO InterfaceInfoUpdateDTO
	 * @return 是否成功
	 */
	@PostMapping("/update")
	public Result<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateDTO interfaceInfoUpdateDTO, HttpServletRequest request) {
		if (interfaceInfoUpdateDTO == null || interfaceInfoUpdateDTO.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateDTO, request);
		return Result.success(isSuccessful);
	}

	/**
	 * 根据 id 获取
	 *
	 * @param id id
	 * @return InterfaceInfo
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
	 * 分页查询
	 *
	 * @param pageRequest 分页查询请求
	 * @return Page<InterfaceInfo>
	 */
	@PostMapping("/list/page")
	public Result<PageVO<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody PageRequest pageRequest) {
		if (pageRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		PageVO<InterfaceInfo> interfaceInfoPage = interfaceInfoService.listInterfaceInfoByPage(pageRequest);
		return Result.success(interfaceInfoPage);
	}

	/**
	 * 模糊查询
	 *
	 * @param fuzzyQueryRequest 模糊查询请求
	 * @return Page<InterfaceInfo>
	 */
	@PostMapping("/list/fuzzy")
	public Result<PageVO<InterfaceInfo>> listInterfaceInfoByFuzzy(@RequestBody FuzzyQueryRequest fuzzyQueryRequest) {
		if (fuzzyQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String keyword = fuzzyQueryRequest.getKeyword();
		if (StrUtil.isBlank(keyword)) {
			return Result.success(null);
		}
		PageVO<InterfaceInfo> list = interfaceInfoService.listInterfaceInfoByFuzzy(fuzzyQueryRequest);
		return Result.success(list);
	}

	/**
	 * 上线接口
	 *
	 * @param idRequest 携带id
	 * @return 是否上线成功
	 */
	@PostMapping("/online")
	public Result<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
		boolean currentUserIsAdmin = UserUtil.hasAdminPermission(request);
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.onlineInterfaceInfo(idRequest, request);
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
		boolean currentUserIsAdmin = UserUtil.hasAdminPermission(request);
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.offlineInterfaceInfo(idRequest, request);
		return Result.success(isSuccessful);
	}

}
