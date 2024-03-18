package com.example.tsd.config;


import com.example.tsd.exception.FileException;
import com.example.tsd.exception.LoginException;
import com.example.tsd.utils.BaseResult;
import com.example.tsd.utils.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionConfig {

    // @ExceptionHandler(RuntimeException.class)
    // public BaseResult globlException(){
    //     return BaseResult.fail(500,"服务器异常");
    // }

    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public BaseResult exception(Exception exception, HttpServletRequest httpServletRequest) throws IOException {
    //     String requestURI = UrlUtil.getRequestUrl(httpServletRequest);
    //     String requestParams = UrlUtil.getRequestParams(httpServletRequest);
    //     String requestBody = UrlUtil.getRequestBody(httpServletRequest);
    //
    //     log.error("运行时报错咯，请求地址：{}、路径参数：{}、请求体：{}", requestURI, requestParams, requestBody);
    //     return BaseResult.fail(500, exception.getMessage());
    // }

    /**
     * 登录异常
     * 可以使用@ExceptionHandler({LoginException.class})
     * 但是作为参数更加灵活
     * @param loginException
     * @return
     */
    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResult loginException(LoginException loginException, HttpServletRequest httpServletRequest) throws IOException {
        String requestURI = UrlUtil.getRequestUrl(httpServletRequest);
        String requestParams = UrlUtil.getRequestParams(httpServletRequest);
        String requestBody = UrlUtil.getRequestBody(httpServletRequest);

        log.error("登录时报错咯，请求地址：{}、路径参数：{}、请求体：{}", requestURI, requestParams, requestBody);
        return BaseResult.fail(loginException.getCode(), loginException.getMessage());
    }

    /**
     * 文件异常
     * @param fileException
     * @return
     */
    @ExceptionHandler(FileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResult fileException(FileException fileException) {
        MultipartFile file = fileException.getFile();
        String originalFilename = null;
        if (file != null) {
            originalFilename = file.getOriginalFilename();
        }
        log.error("文件操作报错咯，文件名：{}", originalFilename);
        return BaseResult.fail(fileException.getCode(), fileException.getMessage());
    }


    /**
     * 参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public BaseResult handleValidatedException(Exception e) {
        if (e instanceof  MethodArgumentNotValidException) {
            log.info("错误类型：MethodArgumentNotValidException");
            MethodArgumentNotValidException ex =(MethodArgumentNotValidException)  e;
            BindingResult bindingResult = ex.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            String message = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            log.info("校验器报错:{}",message);
            return BaseResult.fail(HttpStatus.BAD_REQUEST.value(),message);

        } else  if (e instanceof ConstraintViolationException){
            ConstraintViolationException ex = (ConstraintViolationException) e;
            log.info("错误类型：ConstraintViolationException");
            // result = Result.failure(HttpStatus.BAD_REQUEST.value(),
            //         ex.getConstraintViolations().stream()
            //                 .map(ConstraintViolation::getMessage)
            //                 .collect(Collectors.joining(";"))
            // );
        }else  if (e instanceof BindException) {
            BindException  ex = (BindException ) e;
            log.info("错误类型：BindException");
            // result = Result.failure(HttpStatus.BAD_REQUEST.value(),
            //         ex.getAllErrors().stream()
            //                 .map(ObjectError::getDefaultMessage)
            //                 .collect(Collectors.joining(";"))
            // );
        }else{
            log.error("其他错误");
        }
        ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return BaseResult.fail(HttpStatus.BAD_REQUEST.value(),String.valueOf(objectResponseEntity));
    }

    /**
     * 自定义异常的校验器
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult handlerConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("自定义异常，ConstraintViolationException：{}",ex.getMessage());
        return BaseResult.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}