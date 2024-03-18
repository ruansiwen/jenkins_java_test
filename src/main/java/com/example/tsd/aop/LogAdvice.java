package com.example.tsd.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;


@Aspect
@Component
@Slf4j
public class LogAdvice {

    @Pointcut("execution(* com.example.tsd.controller.*.*(..))")
    public void userServiceMethods() {}

    @Before("userServiceMethods()")
    public void beforeRequest(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        String allArgs = Arrays.toString(joinPoint.getArgs());
        // System.out.println("Logger-->前置通知，方法名："+methodName+"，参数："+args);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String httpMethod = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String params = "";
        if ("POST".equalsIgnoreCase(httpMethod)) {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                params = Arrays.toString(args);
            }
        }

        log.info("AOP捕获请求信息: IP [{}] HTTP_METHOD [{}] URL [{}] QUERY_STRING [{}] PARAMS [{}]",
                remoteAddr, httpMethod, requestURI, queryString, params);
    }

    @AfterReturning(value = "userServiceMethods()",returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result){}

    @Around(value = "userServiceMethods()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return proceed;
    }

    @AfterThrowing(value = "userServiceMethods()",throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){}

    /**
     * 加了@PassToken注解的方法进行增强
     */
    @After("@annotation(com.example.tsd.annotation.PassToken)")
    public void after(JoinPoint joinPoint){
        log.info("aop增强，方法执行后");
    }




}
