package com.xuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.model.entity.InterfaceInfo;

/**
* @author xuan
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-03-28 11:39:00
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

	/**
	 * 校验
	 *
	 * @param interfaceInfo
	 * @param add 是否为创建
	 */
	void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
