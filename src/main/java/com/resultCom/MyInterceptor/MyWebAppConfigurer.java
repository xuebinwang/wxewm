package com.resultCom.MyInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 其实以前都是继承WebMvcConfigurerAdapter类 不过springBoot2.0以上 WebMvcConfigurerAdapter 方法过时，有两种替代方案：
 * 1、继承WebMvcConfigurationSupport
 * 2、实现WebMvcConfigurer
 * 但是继承WebMvcConfigurationSupport会让Spring-boot对mvc的自动配置失效。根据项目情况选择。
 * 现在大多数项目是前后端分离，并没有对静态资源有自动配置的需求所以继承WebMvcConfigurationSupport也未尝不可。
 *
 * @author wxb
 * @date 2020-10-29 11:19
 */
@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestResultinterceptorFilter()).addPathPatterns("/**");
    }
}
