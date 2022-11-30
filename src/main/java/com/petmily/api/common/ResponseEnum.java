package com.petmily.api.common;


import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 에러 시 리턴
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseEnum {

    LOGIN_FAIL("1003", "로그인 실패"),
    DUP_MEMBER("1002", "중복된 회원 정보"),
    INTERNAL_SERVER_ERROR("1001", "서버 에러"),
    ILLEGAL_ARGS_ERROR("1000", "필수값 누락"),
    NO_ACCESS_TOKEN("0002", "토큰 누락"),
    REISSUANCE_ACCESS_TOKEN("0001", "토큰 재발급"),
    SUCCESS("0000", "성공");

    private String code;
    private String msg;

    ResponseEnum(String code , String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
