package com.xuan.client;

import com.xuan.model.entity.InvokeInterface;
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
	 * 根据用户id、path、method查询
	 *
	 * @param userId 用户id
	 * @param path   接口路径
	 * @param method 接口请求方法
	 * @return InvokeInterface
	 */
	@GetMapping(value = "/getByUserIdAndPathAndMethod")
	InvokeInterface selectByUserIdPathAndMethod(@RequestParam(value = "userId") long userId,
	                                            @RequestParam(value = "path") String path,
	                                            @RequestParam(value = "method") String method);

	/**
	 * 调用次数+1
	 *
	 * @param userId          用户id
	 * @param interfaceInfoId 接口id
	 * @return Result<Boolean>
	 */
	@GetMapping(value = "/count")
	Boolean count(@RequestParam(value = "userId") long userId,
	                      @RequestParam(value = "interfaceInfoId") long interfaceInfoId);


}
