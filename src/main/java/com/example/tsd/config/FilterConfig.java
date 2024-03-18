package com.example.tsd.config;

import com.example.tsd.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean UserfilterRegistrationBean(){
        FilterRegistrationBean<UserFilter> filterRegistrationBean = new FilterRegistrationBean<>(new UserFilter());
        filterRegistrationBean.addUrlPatterns("*");
        return filterRegistrationBean;
    }
}
