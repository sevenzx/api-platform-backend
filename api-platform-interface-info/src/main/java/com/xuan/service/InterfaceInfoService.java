package com.xuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.common.DeleteRequest;
import com.xuan.common.IdRequest;
import com.xuan.model.dto.InterfaceInfoAddDTO;
import com.xuan.model.dto.InterfaceInfoQueryDTO;
import com.xuan.model.dto.InterfaceInfoUpdateDTO;
import com.xuan.model.entity.InterfaceInfo;
import com.xuan.model.vo.PageVO;

import javax.servlet.http.HttpServletRequest;

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
	 * @param request             HttpServletRequest
	 * @return 数据库主键id
	 */
	long addInterfaceInfo(InterfaceInfoAddDTO interfaceInfoAddDTO, HttpServletRequest request);

	/**
	 * 删除
	 *
	 * @param deleteRequest DeleteRequest
	 * @param request       HttpServletRequest
	 * @return 是否成功
	 */
	boolean deleteInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request);

	/**
	 * 更新
	 *
	 * @param interfaceInfoUpdateDTO InterfaceInfoUpdateDTO
	 * @param request                HttpServletRequest
	 * @return 是否成功
	 */
	boolean updateInterfaceInfo(InterfaceInfoUpdateDTO interfaceInfoUpdateDTO, HttpServletRequest request);

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
	 * @param request   HttpServletRequest
	 * @return 是否上线成功
	 */
	boolean onlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request);

	/**
	 * 下线接口
	 *
	 * @param idRequest 携带id
	 * @param request   HttpServletRequest
	 * @return 是否下线成功
	 */
	boolean offlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request);

}
