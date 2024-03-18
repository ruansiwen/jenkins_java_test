package com.example.tsd.interceptor;

import com.example.tsd.annotation.PassToken;
import com.example.tsd.exception.LoginException;
import com.example.tsd.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器");
        String uri = request.getRequestURI();
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Method method = ((HandlerMethod) handler).getMethod();
        if(method.isAnnotationPresent(PassToken.class)){
            PassToken PassTokenAnnotation = method.getAnnotation(PassToken.class);
            if(PassTokenAnnotation.required()){
                return true;
            }
        }

        log.info("=========UserInterceptor 拦截器:{}===========================",uri);
        String token = request.getHeader("Token");
        if(StringUtils.isBlank(token)){
            throw new LoginException(400,"没有token");
        }

        Boolean verifyToken = null;
        try {
            verifyToken = JwtUtil.verifyToken(token);
        } catch (Exception e) {
            throw new LoginException(401,"身份过期");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
