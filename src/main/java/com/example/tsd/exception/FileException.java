package com.example.tsd.exception;


import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@Getter
public class FileException extends RuntimeException {
    private static final long serialVersionUID = -3303518302920463234L;
    private Integer code;
    private String message;
    public MultipartFile file;


    public FileException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public FileException(int code, String message, MultipartFile file) {
        super(message);
        this.code = code;
        this.message = message;
        this.file = file;
    }

}
