package com.xuan.exception;

import com.xuan.common.ErrorCode;
import com.xuan.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author xuan
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public Result<?> businessExceptionHandler(BusinessException e) {
		log.error("businessException: " + e.getMessage(), e);
		return Result.error(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public Result<?> runtimeExceptionHandler(RuntimeException e) {
		log.error("runtimeException", e);
		return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), e.getMessage());
	}

}
