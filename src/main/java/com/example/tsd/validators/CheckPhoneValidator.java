package com.example.tsd.validators;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import com.example.tsd.annotation.CheckPhone;
import com.example.tsd.enums.PhoneModeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;


/**
 * 自定义校验器处理逻辑
 */
@Slf4j
public class CheckPhoneValidator implements ConstraintValidator<CheckPhone,String> {

    private PhoneModeEnum phoneMode;

    /**
     * 可以访问已验证约束的属性值，并允许将其存储在验证器的字段中
     * @param constraintAnnotation
     */
    @Override
    public void initialize(CheckPhone constraintAnnotation) {
        String message = constraintAnnotation.message();
        this.phoneMode = constraintAnnotation.value();
        log.info("自定义校验initialize：{},{}",message,phoneMode);
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * 校验器的上下文信息可以获取：1 需要校验的属性路径 2 校验失败时的错误信息 3 校验器是否应该忽略空值
     * 该方法包含实际的验证逻辑
     * @param mobile 被校验的字段的值
     * @param context 校验器的上下文信息
     * @return
     */
    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext context) {
        String s = context.getDefaultConstraintMessageTemplate();
        log.info("校验的字段值：{},{}",mobile,s);
        if(ObjectUtil.isNull(mobile)){
            return true;
        }
        if(ObjectUtil.equal(phoneMode,PhoneModeEnum.IS_MOBILE_HK)){
            return PhoneUtil.isMobileHk(mobile);
        }else if(ObjectUtil.equal(phoneMode,PhoneModeEnum.IS_MOBILE_TW)){
            return PhoneUtil.isMobileTw(mobile);
        }else if(ObjectUtil.equal(phoneMode,PhoneModeEnum.IS_MOBILE_MO)){
            return PhoneUtil.isMobileMo(mobile);
        }else if(ObjectUtil.equal(phoneMode,PhoneModeEnum.IS_MOBILE)){
            boolean mobile1 = PhoneUtil.isMobile(mobile);
            return mobile1;
        }else{
            return PhoneUtil.isPhone(mobile);
        }
        // if(mobile==null || mobile.equals("")){
        //     context.disableDefaultConstraintViolation(); // 禁用默认的错误信息
        //     // 添加自定义错误信息，并且加到校验器上下文
        //     context.buildConstraintViolationWithTemplate("输入值不符合校验规则！").addConstraintViolation();
        //     return true;
        // }
        // return true;
    }
}
