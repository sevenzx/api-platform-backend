package com.xuan.controller;

import com.xuan.common.Result;
import com.xuan.model.entity.InvokeInterface;
import com.xuan.service.InvokeInterfaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xuan
 * @since: 2023/3/30
 */

@RestController
@RequestMapping("/invoke/interface")
public class InvokeInterfaceController {

	@Resource
	InvokeInterfaceService invokeInterfaceService;

	@GetMapping(value = "/getByUserIdAndPathAndMethod")
	public Result<InvokeInterface> selectByUserIdPathAndMethod(@RequestParam(value = "userId") long userId,
	                                                           @RequestParam(value = "path") String path,
	                                                           @RequestParam(value = "method") String method) {
		InvokeInterface invokeInterface = invokeInterfaceService.selectByUserIdPathAndMethod(userId, path, method);
		return Result.success(invokeInterface);
	}

	@GetMapping(value = "/hasNum")
	public Result<Boolean> hasInvokeNum(@RequestParam(value = "userId") long userId,
	                                    @RequestParam(value = "interfaceInfoId") long interfaceInfoId) {
		boolean b = invokeInterfaceService.hasInvokeNum(userId, interfaceInfoId);
		return Result.success(b);
	}

	@GetMapping(value = "/count")
	public Result<Boolean> count(@RequestParam(value = "userId") long userId,
	                             @RequestParam(value = "interfaceInfoId") long interfaceInfoId) {
		boolean b = invokeInterfaceService.invokeInterfaceCount(userId, interfaceInfoId);
		return Result.success(b);
	}

}
