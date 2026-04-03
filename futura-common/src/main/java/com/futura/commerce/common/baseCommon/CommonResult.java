package com.futura.commerce.common.baseCommon;

/**
 * Backward compatibility stub pointing to canonical CommonResult
 */
public class CommonResult<T> extends com.futura.commerce.common.api.CommonResult<T> {
    public CommonResult() {
        super();
    }
    public CommonResult(Integer code, String message, T data) {
        super(code, message, data);
    }
}
