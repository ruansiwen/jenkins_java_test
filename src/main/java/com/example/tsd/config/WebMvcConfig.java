package com.example.tsd.config;

import com.example.tsd.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


/**
 * WebMvcConfigurer 适用于简单的定制化配置
 * WebMvcConfigurationSupport 则适用于更深入的定制和自定义。
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(loginInterceptor);
        String[] excludePatterns = new String[]{"/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**",
                "/api", "/api-docs", "/api-docs/**", "/doc.html/**","/v3/api-docs/**","/swagger-ui/**","/v3/api-docs"};
        interceptorRegistration.excludePathPatterns("/error");
        interceptorRegistration.excludePathPatterns(excludePatterns);
        interceptorRegistration.excludePathPatterns("/static/**");
        interceptorRegistration.excludePathPatterns("/demo/**");
        interceptorRegistration.excludePathPatterns("/**/*login*/**");
        interceptorRegistration.excludePathPatterns("/**/*register*/**");
    }


    /**
     * 静态资源配置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        // 静态资源从这里取消拦截
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }


}
