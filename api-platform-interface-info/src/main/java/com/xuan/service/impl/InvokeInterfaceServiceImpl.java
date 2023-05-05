package com.xuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.common.ErrorCode;
import com.xuan.exception.BusinessException;
import com.xuan.mapper.InvokeInterfaceMapper;
import com.xuan.model.entity.InvokeInterface;
import com.xuan.service.InvokeInterfaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuan
 * @description 针对表【invoke_interface(用户调用接口关系表)】的数据库操作Service实现
 * @createDate 2023-03-30 15:31:14
 */
@Service
public class InvokeInterfaceServiceImpl extends ServiceImpl<InvokeInterfaceMapper, InvokeInterface>
		implements InvokeInterfaceService {

	@Resource
	InvokeInterfaceMapper invokeInterfaceMapper;

	@Override
	public InvokeInterface selectByUserIdPathAndMethod(long userId, String path, String method) {
		if (userId <= 0 || path == null || method == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}

		return invokeInterfaceMapper.selectByUserIdPathAndMethod(userId, path, method);
	}

	@Override
	public boolean hasInvokeNum(long userId, long interfaceInfoId) {
		if (userId <= 0 || interfaceInfoId <= 0) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}

		LambdaQueryWrapper<InvokeInterface> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(InvokeInterface::getUserId, userId)
				.eq(InvokeInterface::getInterfaceInfoId, interfaceInfoId)
				.gt(InvokeInterface::getLeftNum, 0);

		InvokeInterface invokeInterface = invokeInterfaceMapper.selectOne(queryWrapper);
		return invokeInterface != null;
	}

	@Override
	public boolean invokeInterfaceCount(long userId, long interfaceInfoId) {
		if (userId <= 0 || interfaceInfoId <= 0) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}

		LambdaUpdateWrapper<InvokeInterface> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(InvokeInterface::getUserId, userId)
				.eq(InvokeInterface::getInterfaceInfoId, interfaceInfoId)
				.gt(InvokeInterface::getLeftNum, 0)
				.setSql("left_num = left_num -1, total_num = total_num + 1");

		int updateCount = invokeInterfaceMapper.update(null, updateWrapper);
		return updateCount > 0;
	}

}




