package com.example.tsd.utils;


import com.example.tsd.enums.ResultEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Tag(name = "BaseResult")
@Data
public class BaseResult<T> {
    public int code;
    public String message;
    public T data;

    protected BaseResult() {
    }

    protected BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    protected BaseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    protected BaseResult(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    protected BaseResult(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    public static <T> BaseResult<T> success(ResultEnum resultEnum, T data) {
        return new BaseResult(resultEnum, data);
    }

    public static <T> BaseResult<T> success(ResultEnum resultEnum) {
        return new BaseResult(ResultEnum.SUCCESS);
    }

    public static BaseResult fail(ResultEnum resultEnum) {
        return new BaseResult(ResultEnum.FAILED);
    }

    public static <T> BaseResult<T> fail(int code, String message) {
        return new BaseResult<>(code, message);
    }
}
