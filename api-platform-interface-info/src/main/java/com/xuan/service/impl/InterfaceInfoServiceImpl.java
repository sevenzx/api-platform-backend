package com.xuan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.common.*;
import com.xuan.model.dto.InterfaceInfoAddDTO;
import com.xuan.model.dto.InterfaceInfoUpdateDTO;
import com.xuan.exception.BusinessException;
import com.xuan.mapper.InterfaceInfoMapper;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.model.enums.InterfaceInfoStatusEnum;
import com.xuan.model.vo.PageVO;
import com.xuan.model.vo.UserVO;
import com.xuan.service.CommonService;
import com.xuan.service.InterfaceInfoService;
import com.xuan.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @author xuan
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-03-28 11:39:00
 */

@Slf4j
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
		implements InterfaceInfoService {


	@Override
	public long addInterfaceInfo(InterfaceInfoAddDTO interfaceInfoAddDTO, HttpServletRequest request) {
		if (interfaceInfoAddDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoAddDTO, interfaceInfo);
		// 校验
		this.validInterfaceInfo(interfaceInfo, true);
		// 设置当前用户id
		UserVO userVO = UserUtil.currentUser(request);
		interfaceInfo.setUserId(userVO.getId());
		boolean result = this.save(interfaceInfo);
		if (!result) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR);
		}
		return interfaceInfo.getId();
	}

	@Override
	public boolean deleteInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0L) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Long id = deleteRequest.getId();
		this.checkPermission(id, request);
		return this.removeById(id);
	}

	@Override
	public boolean updateInterfaceInfo(InterfaceInfoUpdateDTO interfaceInfoUpdateDTO, HttpServletRequest request) {
		if (interfaceInfoUpdateDTO == null || interfaceInfoUpdateDTO.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoUpdateDTO, interfaceInfo);
		// 参数校验
		this.validInterfaceInfo(interfaceInfo, false);
		this.checkPermission(interfaceInfoUpdateDTO.getId(), request);
		return this.updateById(interfaceInfo);
	}

	@Override
	public PageVO<InterfaceInfo> listInterfaceInfoByPage(PageRequest pageRequest) {
		Page<InterfaceInfo> queryPage = CommonService.listByPage(this, pageRequest);
		return new PageVO<>(queryPage.getRecords(), queryPage.getCurrent(), queryPage.getSize(), queryPage.getTotal());
	}

	@Override
	public PageVO<InterfaceInfo> listInterfaceInfoByFuzzy(FuzzyQueryRequest fuzzyQueryRequest) {

		Page<InterfaceInfo> queryPage = CommonService.listByFuzzy(this, fuzzyQueryRequest);

		return new PageVO<>(queryPage.getRecords(), queryPage.getCurrent(), queryPage.getSize(), queryPage.getTotal());
	}

	@Override
	public boolean onlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
		this.checkAdmin(request);
		if (idRequest == null || idRequest.getId() < 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 判断接口是否存在
		long id = idRequest.getId();
		InterfaceInfo originInterfaceInfo = this.getById(id);
		if (originInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}

		// 判断接口是否能使用
		// TODO 根据测试地址来调用
		// 这里我先用固定的方法进行测试，后面来改

		// 更新数据库
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setId(id);
		interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
		return this.updateById(interfaceInfo);
	}

	@Override
	public boolean offlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
		this.checkAdmin(request);
		if (idRequest == null || idRequest.getId() < 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 判断接口是否存在
		long id = idRequest.getId();
		InterfaceInfo originInterfaceInfo = this.getById(id);
		if (originInterfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 更新数据库
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setId(id);
		interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
		return this.updateById(interfaceInfo);
	}

	private void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
		if (interfaceInfo == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		String name = interfaceInfo.getName();
		String description = interfaceInfo.getDescription();
		String path = interfaceInfo.getPath();
		String requestHeader = interfaceInfo.getRequestHeader();
		String responseHeader = interfaceInfo.getResponseHeader();
		String method = interfaceInfo.getMethod();
		Long userId = interfaceInfo.getUserId();

		// 创建时，所有参数必须非空
		if (add) {
			if (StrUtil.hasBlank(name, description, path, requestHeader, responseHeader, method) || ObjectUtil.isNotNull(userId)) {
				throw new BusinessException(ErrorCode.PARAMS_ERROR);
			}
		}
		if (StrUtil.isNotBlank(name) && name.length() > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "名字过长");
		}
		if (StrUtil.isNotBlank(description) && description.length() > 512) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
		}
	}

	private void checkPermission(Long interfaceInfoId, HttpServletRequest request) {
		UserVO userVO = UserUtil.currentUser(request);
		// 判断是否存在
		InterfaceInfo interfaceInfo = this.getById(interfaceInfoId);
		if (interfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可操作
		if (!interfaceInfo.getUserId().equals(userVO.getId()) && !UserUtil.hasAdminPermission(request)) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
	}

	private void checkAdmin(HttpServletRequest request) {
		boolean currentUserIsAdmin = UserUtil.hasAdminPermission(request);
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
	}
}




