package com.resultCom.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author wxb
 * 返回统一实体结果，序列化
 * @date 2020-10-22 15:00
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ResultVo implements Serializable {
    private Integer code;
    private String message;
    private Object object;

    public ResultVo(Integer code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }

    public ResultVo(Object data, RestultCode restultCode){
        this.object = data;
        this.code = restultCode.code();
        this.message = restultCode.message();
    }
    public ResultVo(RestultCode restultCode){
        this.code = restultCode.code();
        this.message = restultCode.message();
    }
    public static ResultVo success(){
        return new ResultVo(RestultCode.SUCCESS);
    }
    public static ResultVo success(Object data){
        return new ResultVo(data, RestultCode.SUCCESS);
    }
    public static ResultVo failure(){
        return new ResultVo(RestultCode.FAILURE);
    }
    public static ResultVo failure(Object data){
        return new ResultVo(data, RestultCode.FAILURE);
    }
    //拦截器处理，当返回body异常
    public static ResultVo failure(Integer code, String message, Object object){
        return new ResultVo(code, message, object);
    }
}
