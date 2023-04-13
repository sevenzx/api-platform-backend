package com.xuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuan.client.UserClient;
import com.xuan.model.dto.InterfaceInfoAddDTO;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.common.*;
import com.xuan.exception.BusinessException;

import com.xuan.model.vo.PageVO;
import com.xuan.service.InterfaceInfoService;
import com.xuan.model.dto.InterfaceInfoQueryDTO;
import com.xuan.model.dto.InterfaceInfoUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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


	/**
	 * 新增
	 *
	 * @param interfaceInfoAddDTO InterfaceInfoAddDTO
	 * @return 数据库主键id
	 */
	@PostMapping("/add")
	public Result<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddDTO interfaceInfoAddDTO) {
		if (interfaceInfoAddDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long id = interfaceInfoService.addInterfaceInfo(interfaceInfoAddDTO);
		return Result.success(id);
	}

	/**
	 * 删除
	 *
	 * @param deleteRequest DeleteRequest
	 * @return 是否成功
	 */
	@PostMapping("/delete")
	public Result<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.deleteInterfaceInfo(deleteRequest);
		return Result.success(isSuccessful);
	}

	/**
	 * 更新
	 *
	 * @param interfaceInfoUpdateDTO InterfaceInfoUpdateDTO
	 * @return 是否成功
	 */
	@PostMapping("/update")
	public Result<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateDTO interfaceInfoUpdateDTO) {
		if (interfaceInfoUpdateDTO == null || interfaceInfoUpdateDTO.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateDTO);
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
	 * 获取列表（仅管理员可使用）
	 *
	 * @param interfaceInfoQueryDTO InterfaceInfoQueryDTO
	 * @return List<InterfaceInfo>
	 */
	@GetMapping("/list")
	public Result<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
		boolean currentUserIsAdmin = userClient.currentUserIsAdmin();
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
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
	 * @param interfaceInfoQueryDTO InterfaceInfoQueryDTO
	 * @return Page<InterfaceInfo>
	 */
	@PostMapping("/list/page")
	public Result<PageVO<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
		if (interfaceInfoQueryDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		PageVO<InterfaceInfo> interfaceInfoPage = interfaceInfoService.listInterfaceInfoByPage(interfaceInfoQueryDTO);
		return Result.success(interfaceInfoPage);
	}

	/**
	 * 上线接口
	 *
	 * @param idRequest 携带id
	 * @return 是否上线成功
	 */
	@PostMapping("/online")
	public Result<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
		boolean currentUserIsAdmin = userClient.currentUserIsAdmin();
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.onlineInterfaceInfo(idRequest);
		return Result.success(isSuccessful);
	}

	/**
	 * 下线接口
	 *
	 * @param idRequest 携带id
	 * @return 是否下线成功
	 */
	@PostMapping("/offline")
	public Result<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
		boolean currentUserIsAdmin = userClient.currentUserIsAdmin();
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
		boolean isSuccessful = interfaceInfoService.offlineInterfaceInfo(idRequest);
		return Result.success(isSuccessful);
	}

}
