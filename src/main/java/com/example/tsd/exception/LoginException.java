package com.example.tsd.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.annotation.Annotation;

@Data
@Getter
public class LoginException extends RuntimeException {
    private static final long serialVersionUID = -3303518302920463235L;
    private Integer code;

    private String message;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param code
     * @param message
     */
    public LoginException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }


    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}