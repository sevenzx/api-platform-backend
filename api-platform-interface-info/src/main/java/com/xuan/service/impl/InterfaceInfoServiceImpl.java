package com.xuan.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.client.UserClient;
import com.xuan.common.DeleteRequest;
import com.xuan.common.ErrorCode;
import com.xuan.common.IdRequest;
import com.xuan.common.Result;
import com.xuan.model.dto.InterfaceInfoAddDTO;
import com.xuan.model.dto.InterfaceInfoQueryDTO;
import com.xuan.model.dto.InterfaceInfoUpdateDTO;
import com.xuan.exception.BusinessException;
import com.xuan.mapper.InterfaceInfoMapper;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.model.enums.InterfaceInfoStatusEnum;
import com.xuan.model.vo.PageVO;
import com.xuan.model.vo.UserVO;
import com.xuan.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.xuan.constant.CommonConstant.MAX_PAGE_SIZE;

/**
 * @author xuan
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-03-28 11:39:00
 */

@Slf4j
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
		implements InterfaceInfoService {

	@Resource
	private UserClient userClient;

	@Override
	public long addInterfaceInfo(InterfaceInfoAddDTO interfaceInfoAddDTO) {
		if (interfaceInfoAddDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoAddDTO, interfaceInfo);
		// 校验
		this.validInterfaceInfo(interfaceInfo, true);
		// 设置当前用户id
		Result<UserVO> userVOResult = userClient.getCurrentUser();
		UserVO userVO = userVOResult.getData();
		interfaceInfo.setUserId(userVO.getId());
		boolean result = this.save(interfaceInfo);
		if (!result) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR);
		}
		return interfaceInfo.getId();
	}

	@Override
	public boolean deleteInterfaceInfo(DeleteRequest deleteRequest) {
		if (deleteRequest == null || deleteRequest.getId() <= 0L) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Long id = deleteRequest.getId();
		this.checkPermission(id);
		return this.removeById(id);
	}

	@Override
	public boolean updateInterfaceInfo(InterfaceInfoUpdateDTO interfaceInfoUpdateDTO) {
		if (interfaceInfoUpdateDTO == null || interfaceInfoUpdateDTO.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoUpdateDTO, interfaceInfo);
		// 参数校验
		this.validInterfaceInfo(interfaceInfo, false);
		this.checkPermission(interfaceInfoUpdateDTO.getId());
		return this.updateById(interfaceInfo);
	}

	@Override
	public PageVO<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
		if (interfaceInfoQueryDTO == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoQueryDTO, interfaceInfoQuery);

		// 基础字段
		long current = interfaceInfoQueryDTO.getCurrent();
		long pageSize = interfaceInfoQueryDTO.getPageSize();
		boolean ascend = interfaceInfoQueryDTO.isAscend();
		boolean needTotal = interfaceInfoQueryDTO.isNeedTotal();
		String sortField = StrUtil.toUnderlineCase(interfaceInfoQueryDTO.getSortField());
		String description = interfaceInfoQuery.getDescription();
		// description 需支持模糊搜索
		interfaceInfoQuery.setDescription(null);
		// 限制爬虫
		if (pageSize > MAX_PAGE_SIZE) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "pageSize不得超过" + MAX_PAGE_SIZE);
		}
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
		queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
		queryWrapper.orderBy(StringUtils.isNotBlank(sortField), ascend, sortField);

		Page<InterfaceInfo> interfaceInfoPage = new Page<>(current, pageSize);
		// 设置是否需要翻页
		interfaceInfoPage.setSearchCount(needTotal);
		try {
			interfaceInfoPage = this.page(interfaceInfoPage, queryWrapper);
		} catch (Exception e) {
			log.info("listUserByPage 中 sortField: {}", sortField);
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}
		return new PageVO<>(interfaceInfoPage.getRecords(), interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
	}

	@Override
	public boolean onlineInterfaceInfo(IdRequest idRequest) {
		this.checkAdmin();
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
		// UserVO user = new UserVO();
		// user.setUsername("MARS");
		// String name = xuanApiClient.getNameByPostWithJson(user);
		// if (StrUtil.isBlank(name)) {
		// 	throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
		// }

		// 更新数据库
		InterfaceInfo interfaceInfo = new InterfaceInfo();
		interfaceInfo.setId(id);
		interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
		return this.updateById(interfaceInfo);
	}

	@Override
	public boolean offlineInterfaceInfo(IdRequest idRequest) {
		this.checkAdmin();
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
		String url = interfaceInfo.getUrl();
		String requestHeader = interfaceInfo.getRequestHeader();
		String responseHeader = interfaceInfo.getResponseHeader();
		String method = interfaceInfo.getMethod();
		Long userId = interfaceInfo.getUserId();

		// 创建时，所有参数必须非空
		if (add) {
			if (StrUtil.hasBlank(name, description, url, requestHeader, responseHeader, method) || ObjectUtil.isNotNull(userId)) {
				throw new BusinessException(ErrorCode.PARAMS_ERROR);
			}
		}
		if (StringUtils.isNotBlank(name) && name.length() > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "名字过长");
		}
		if (StringUtils.isNotBlank(description) && description.length() > 512) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
		}
	}

	private void checkPermission(Long interfaceInfoId) {
		Result<UserVO> userVOResult = userClient.getCurrentUser();
		UserVO userVO = userVOResult.getData();
		// 判断是否存在
		InterfaceInfo interfaceInfo = this.getById(interfaceInfoId);
		if (interfaceInfo == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 仅本人或管理员可操作
		if (!interfaceInfo.getUserId().equals(userVO.getId()) && !userClient.currentUserIsAdmin()) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
	}

	private void checkAdmin() {
		boolean currentUserIsAdmin = userClient.currentUserIsAdmin();
		if (!currentUserIsAdmin) {
			throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
		}
	}
}




