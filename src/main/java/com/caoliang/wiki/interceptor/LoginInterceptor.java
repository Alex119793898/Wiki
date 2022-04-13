/*
package com.caoliang.wiki.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("------------- LogInterceptor 开始 -------------");
        LOG.info("请求地址: {} {}",request.getRequestURL().toString(),request.getMethod());
        LOG.info("远程地址: {}",request.getRemoteAddr());

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime",startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        LOG.info("------------- LogInterceptor 结束 耗时：{} ms -------------",System.currentTimeMillis() - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
*/
