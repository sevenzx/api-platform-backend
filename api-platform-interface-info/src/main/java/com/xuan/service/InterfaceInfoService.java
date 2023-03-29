package com.xuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.common.DeleteRequest;
import com.xuan.common.IdRequest;
import com.xuan.dto.InterfaceInfoAddDTO;
import com.xuan.dto.InterfaceInfoQueryDTO;
import com.xuan.dto.InterfaceInfoUpdateDTO;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.model.vo.PageVO;

/**
 * @author xuan
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2023-03-28 11:39:00
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {


	/**
	 * 新增
	 *
	 * @param interfaceInfoAddDTO InterfaceInfoAddDTO
	 * @return 数据库主键id
	 */
	long addInterfaceInfo(InterfaceInfoAddDTO interfaceInfoAddDTO);

	/**
	 * 删除
	 *
	 * @param deleteRequest DeleteRequest
	 * @return 是否成功
	 */
	boolean deleteInterfaceInfo(DeleteRequest deleteRequest);

	/**
	 * 更新
	 *
	 * @param interfaceInfoUpdateDTO InterfaceInfoUpdateDTO
	 * @return 是否成功
	 */
	boolean updateInterfaceInfo(InterfaceInfoUpdateDTO interfaceInfoUpdateDTO);

	/**
	 * 分页获取列表
	 *
	 * @param interfaceInfoQueryDTO InterfaceInfoQueryDTO
	 * @return Page<InterfaceInfo>
	 */
	PageVO<InterfaceInfo> listInterfaceInfoByPage(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

	/**
	 * 上线接口
	 *
	 * @param idRequest 携带id
	 * @return 是否上线成功
	 */
	boolean onlineInterfaceInfo(IdRequest idRequest);

	/**
	 * 下线接口
	 *
	 * @param idRequest 携带id
	 * @return 是否下线成功
	 */
	boolean offlineInterfaceInfo(IdRequest idRequest);

}
