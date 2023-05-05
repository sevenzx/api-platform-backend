package com.xuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.model.entity.InvokeInterface;

/**
 * @author xuan
 * @description 针对表【invoke_interface(用户调用接口关系表)】的数据库操作Service
 * @createDate 2023-03-30 15:31:14
 */
public interface InvokeInterfaceService extends IService<InvokeInterface> {

	/**
	 * 根据用户id、path、method查询
	 *
	 * @param userId 用户id
	 * @param path   接口路径
	 * @param method 接口请求方法
	 * @return InvokeInterface
	 */
	InvokeInterface selectByUserIdPathAndMethod(long userId, String path, String method);


	/**
	 * 该用户是否能够调用该接口
	 *
	 * @param userId          用户id
	 * @param interfaceInfoId 接口id
	 * @return boolean
	 */
	boolean hasInvokeNum(long userId, long interfaceInfoId);


	/**
	 * 接口调用成功后计数
	 *
	 * @param userId          用户id
	 * @param interfaceInfoId 接口id
	 * @return boolean
	 */
	boolean invokeInterfaceCount(long userId, long interfaceInfoId);

}
