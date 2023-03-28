package com.xuan.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 返回前端对象
 *
 * @author: xuan
 * @since: 2023/2/17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
	private int code;
	private T data;
	private String message;

	public Result(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 成功
	 *
	 * @param data 数据
	 * @param <T>  泛型
	 * @return Result<T>
	 */
	public static <T> Result<T> success(T data) {
		return new Result<>(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
	}

	/**
	 * 失败
	 *
	 * @param errorCode 枚举ErrorCode
	 * @param <T>       泛型
	 * @return Result<T>
	 */
	public static <T> Result<T> error(ErrorCode errorCode) {
		return new Result<>(errorCode.getCode(), errorCode.getMessage());
	}


	/**
	 * 供异常处理器使用
	 *
	 * @param code    状态码
	 * @param message 信息
	 * @param <T>     泛型
	 * @return Result<T>
	 */
	public static <T> Result<T> error(int code, String message) {
		return new Result<>(code, message);
	}

}
