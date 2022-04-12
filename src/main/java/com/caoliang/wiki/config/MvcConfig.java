package com.caoliang.wiki.config;

import com.caoliang.wiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /*
    * 将拦截器注册到spring ioc容器
    * return new LoginInterceptor()
    */
    @Bean
    public LoginInterceptor myInterceptor(){
        return new LoginInterceptor();
    }
    //@Resource
    //LoginInterceptor loginInterceptor;

    /*
    * 重写该方法
    * 往拦截器链中添加拦截器
    * @params registry 拦截器链
    * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor()).addPathPatterns("/**");
    }
}
