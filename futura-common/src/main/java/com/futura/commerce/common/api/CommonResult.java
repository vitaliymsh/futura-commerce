package com.futura.commerce.common.api;

import lombok.Data;

/**
 * Universal API Response Wrapper
 *
 * @author Vitalii
 */
@Data
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    public CommonResult() {}

    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 200 success response with data
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(200, message, data);
    }

    /**
     * 200 success response with data only
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "Operation successful", data);
    }

    /**
     * 200 success response without data
     */
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<>(200, message, null);
    }

    /**
     * 500 failure response
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(500, message, null);
    }

    public static <T> CommonResult<T> failed() {
        return new CommonResult<>(500, "Operation failed", null);
    }

    /**
     * 400 validation failed response
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<>(400, message, null);
    }

    /**
     * 401 unauthorized response with custom message
     */
    public static <T> CommonResult<T> unauthorized(String message) {
        return new CommonResult<>(401, message, null);
    }

    /**
     * 401 unauthorized default response
     */
    public static <T> CommonResult<T> unauthorized() {
        return unauthorized("Unauthorized or authentication failed");
    }

    /**
     * 404 not found response
     */
    public static <T> CommonResult<T> notFound() {
        return new CommonResult<>(404, "Resource not found", null);
    }

    /**
     * 403 forbidden response
     */
    public static <T> CommonResult<T> forbidden(String message) {
        return new CommonResult<>(403, message, null);
    }
}
