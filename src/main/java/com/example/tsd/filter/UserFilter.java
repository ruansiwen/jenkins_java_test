package com.example.tsd.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;


@Slf4j
public class UserFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String localAddr = servletRequest.getLocalAddr();
        log.info("过滤器: {}",localAddr);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
