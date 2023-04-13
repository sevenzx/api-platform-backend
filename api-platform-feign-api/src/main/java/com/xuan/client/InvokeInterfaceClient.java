package com.xuan.client;

import com.xuan.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: xuan
 * @since: 2023/3/30
 */

@FeignClient(value = "api-platform-interface-info", path = "/api/invoke/interface")
public interface InvokeInterfaceClient {

	/**
	 * 是否有调用次数
	 *
	 * @param userId          用户id
	 * @param interfaceInfoId 接口id
	 * @return Result<Boolean>
	 */
	@GetMapping(value = "/hasNum")
	Result<Boolean> hasInvokeNum(@RequestParam(value = "userId") long userId,
	                             @RequestParam(value = "interfaceInfoId") long interfaceInfoId);

	/**
	 * 调用次数+1
	 *
	 * @param userId          用户id
	 * @param interfaceInfoId 接口id
	 * @return Result<Boolean>
	 */
	@GetMapping(value = "/count")
	Result<Boolean> count(@RequestParam(value = "userId") long userId,
	                      @RequestParam(value = "interfaceInfoId") long interfaceInfoId);


}
