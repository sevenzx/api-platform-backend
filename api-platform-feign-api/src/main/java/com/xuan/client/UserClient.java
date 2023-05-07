package com.xuan.client;

import com.xuan.model.vo.InvokeInterfaceUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: xuan
 * @since: 2023/3/28
 */

@FeignClient(value = "api-platform-user", path = "/api/user")
public interface UserClient {

	/**
	 * 通过key获取secret和用户id
	 *
	 * @param userKey user key
	 * @return Result<InvokeInterfaceUserVO>
	 */
	@GetMapping("/get/secret")
	InvokeInterfaceUserVO getSecretByKey(@RequestParam(value = "userKey") String userKey);

}
