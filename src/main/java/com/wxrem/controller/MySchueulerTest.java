package com.wxrem.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wxb
 * @date 2020-07-14 09:31
 * 测试 springboot使用 @EnableScheduling、@Scheduled + cron 开启定时任务
 * DemoApplication启动类加注解：@EnableScheduling
 * 使同一个线程中串行执行
 * cron 表达式参考：  https://blog.csdn.net/weixin_39249427/article/details/107331415
 */

@Component
public class MySchueulerTest {

    @Scheduled(cron = "10 * * * * ?")
    public void task(){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date())+"*********A任务每分10秒钟时，执行一次进入测试");

    }
    @Scheduled(cron = "30 * * * * ?")
    public void task2(){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date())+"*********A任务每分30秒钟时，执行一次进入测试");

    }
}
