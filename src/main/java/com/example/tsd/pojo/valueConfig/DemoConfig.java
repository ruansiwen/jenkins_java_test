package com.example.tsd.pojo.valueConfig;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties("file.upload")
public class DemoConfig {
    public String directory;
}
