package com.resultCom.vo;

/**
 * 返回请求状态码枚举
 */
public enum RestultCode {
    SUCCESS(1, "成功"),
    FAILURE(2, "成功"),
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    USER_NOT_LOGIN(1001, "用户未登录"),
    USER_LOGIN_ERROR(1002, "用户登录失败"),
    USER_NOT_EXEIT(1003, "用户不存在"),
    USER_HAD_EXEITED(1004, "用户已存在");

    private Integer code;
    private String message;
    RestultCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    public Integer code() {
        return this.code;
    }
    public String message() {
        return this.message;
    }




}
