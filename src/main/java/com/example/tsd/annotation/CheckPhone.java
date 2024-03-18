package com.example.tsd.annotation;

import com.example.tsd.enums.PhoneModeEnum;
import com.example.tsd.validators.CheckPhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义校验注解，指定特定的校验器
 */
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckPhoneValidator.class)
public @interface CheckPhone {

    /**
     * 不合法时 抛出异常信息
     */
    String message() default "默认提示";

    /**
     * 分组
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * 在ValidatorFactory初始化期间定义约束验证器有效负载
     * @return
     */
    Class<? extends Payload>[] payload() default {};


    /**
     * 指定使用什么逻辑验证手机号
     */
    PhoneModeEnum value();


    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CheckPhone[] value();
    }
}
