package com.resultCom.MyInterceptor;

import com.resultCom.annotation.MyResponseResult;
import com.resultCom.vo.ResultVo;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 响应拦截
 * @author wxb
 * @date 2020-10-22 17:21
 */
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    //标记
    private static final String RESPONSE_REQUEST_ANN = "RESPONSE_REQUEST_ANN";

    //是否请求 包含了自定义注解标记，没有就直接返回，不需要重写返回体
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest hsr = sra.getRequest();
        MyResponseResult resp = (MyResponseResult) hsr.getAttribute(RESPONSE_REQUEST_ANN);
        return resp == null? false: true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //计入返回体，重写部分
//        if (body instanceof ERRorR){
//            ErrorResult err = (ErrorResult) body;
//            return ResultVo.failure(err.get);
//        }
        return ResultVo.success(body);
    }
}
