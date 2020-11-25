package com.resultCom.control;

import com.resultCom.annotation.MyResponseResult;
import com.resultCom.service.UserService;
import com.resultCom.vo.ResultVo;
import com.resultCom.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wxb
 * @date 2020-10-22 16:00
 */

@RestController
@RequestMapping("/userInfo")
public class UserInfoContrl {
    @Autowired
    private UserService userService;

    @GetMapping(value="getByUserId/{userId}",produces="application/json;charset=utf-8")
    public UserInfo getByUserId(@PathVariable String userId){
        return userService.getByUserId(userId);
    }

    /**
     * 构造方法这样的包装是不是很麻烦，我们可以优化一下。看getByUserId2
     * @param userId
     * @return
     */
    @GetMapping("getByUserId1/{userId}")
    @MyResponseResult
    public UserInfo getByUserId1(@PathVariable("userId") String userId){
//        ResultVo resultVo = new ResultVo(userService.getByUserId(userId), RestultCode.SUCCESS);
        return userService.getByUserId(userId);
    }

    /**
     * 几个问题：
     *
     * 1、每个方法的返回都是Result封装对象，没有业务含义
     * 2、在业务代码中，成功的时候我们调用Result.success，异常错误调用Result.failure。是不是很多余
     * @param userId
     * @return
     */
    @GetMapping("getByUserId2/{userId}")
    @MyResponseResult
    public ResultVo getByUserId2(@PathVariable("userId") String userId){
        return ResultVo.success(userService.getByUserId(userId));
    }
    /**
     * 对于getByUserId2接口，实现返回原生对象，自动封装标准，
     * 如getByUserId接口一般，我们使用拦截器将我们注释的接口返回结果再统一包装
     * 1、定义一个注解@ResponseResult，表示这个接口返回的值需要包装一下
     * 2、拦截请求，判断此请求是否需要被@ResponseResult注解
     * 3、核心步骤就是实现接口ResponseBodyAdvice和@ControllerAdvice，判断是否需要包装返回值，如果需要，就把Controller接口的返回值进行重写。
     */

}
