package com.petmily.api.common;

/**
 * RestAPI SUCCESS return format
 * @param <T>
 */
public class SuccessResponse<T> {
    private String code = ResponseEnum.SUCCESS.getCode();
    private String msg = ResponseEnum.SUCCESS.getMsg();
    private T data;

    public SuccessResponse() {
    }

    public SuccessResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setStatus(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
