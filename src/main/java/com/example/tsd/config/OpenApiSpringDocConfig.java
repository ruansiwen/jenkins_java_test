package com.example.tsd.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;

/**
 * SpringDoc 配置类
 *
 * @author Jack魏
 * @since 2023/1/8 16:51
 */


@Configuration
public class OpenApiSpringDocConfig {
    private Info info(){
        return new Info()
                .title("API接口文档")
                .description("ubuntu测试")
                .version("v1.0.0");
    }
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(info());
    }
}
