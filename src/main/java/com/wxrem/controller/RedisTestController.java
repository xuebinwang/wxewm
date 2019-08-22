package com.wxrem.controller;

import com.wxrem.common.redis.RedisUtils;
import com.wxrem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * redis 停止服务和启动服务
 * redis-server --service-stop
 * redis-server --service-start
 * 卸载服务：redis-server --service-uninstall
 *
 */
@Controller
public class RedisTestController {
    @Autowired
    @Qualifier(value = "redisUtil")
    private RedisUtils redisUtils;

    /**
     * 测试redis
     * http://localhost:8000/redis/testAdd?age=21&name="问问"
     * @param user
     * @return
     */
    @RequestMapping("/testAdd")
    @ResponseBody
    public String testAdd(User user){
        String key = "00000";
        User users = new User();
        users.setAge(user.getAge());
        users.setName(user.getName());
        redisUtils.set(key,users);
        System.out.println(redisUtils.get(key));
        return redisUtils.get(key).toString();
    }

}
