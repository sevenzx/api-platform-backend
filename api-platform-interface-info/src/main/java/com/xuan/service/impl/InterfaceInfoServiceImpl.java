package com.xuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.common.ErrorCode;
import com.xuan.exception.BusinessException;
import com.xuan.mapper.InterfaceInfoMapper;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author xuan
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-03-28 11:39:00
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

	@Override
	public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
		if (interfaceInfo == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		String name = interfaceInfo.getName();
		String description = interfaceInfo.getDescription();
		String url = interfaceInfo.getUrl();
		String requestHeader = interfaceInfo.getRequestHeader();
		String responseHeader = interfaceInfo.getResponseHeader();
		Integer status = interfaceInfo.getStatus();
		String method = interfaceInfo.getMethod();
		Long userId = interfaceInfo.getUserId();

		// 创建时，所有参数必须非空
		// if (add) {
		// 	if (StringUtils.isAnyBlank(name, description, url, requestHeader, responseHeader, method) || ObjectUtils.anyNull(userId, status)) {
		// 		throw new BusinessException(ErrorCode.PARAMS_ERROR);
		// 	}
		// }
		if (StringUtils.isNotBlank(name) && name.length() > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "名字过长");
		}
		if (StringUtils.isNotBlank(description) && description.length() > 512) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
		}
	}

}




