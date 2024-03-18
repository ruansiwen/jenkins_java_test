package com.example.tsd.pojo.valueConfig;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class FileConfig {

    @Value("${file.upload.directory}")
    public String directory;

    /**
     * 分片文件存储的地方
     */
    @Value("${file.upload.chunkFile}")
    public String chunkFile;

}
