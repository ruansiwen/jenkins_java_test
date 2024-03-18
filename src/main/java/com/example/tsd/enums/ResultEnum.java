package com.example.tsd.enums;


import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(200, "请求成功"),
    FAILED(500,"请求失败");

    public final int code;
    public final String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
