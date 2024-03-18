package com.example.tsd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
// import springfox.documentation.oas.annotations.EnableOpenApi;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.example.tsd.mapper")
public class TsdApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsdApplication.class, args);
    }

}
