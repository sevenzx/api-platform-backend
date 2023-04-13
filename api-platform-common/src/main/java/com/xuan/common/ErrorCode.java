package com.xuan.common;

/**
 * 错误码
 *
 * @author: xuan
 * @since: 2023/2/17
 */
public enum ErrorCode {

    // 成功
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "request parameter error"),
    NOT_LOGIN_ERROR(40100, "not log in"),
    NO_PERMISSION_ERROR(40101, "no permission"),
    NOT_FOUND_ERROR(40400, "not found"),
    FORBIDDEN_ERROR(40300, "forbidden"),
    SYSTEM_ERROR(50000, "system error"),
    OPERATION_ERROR(50001, "operation failed");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
