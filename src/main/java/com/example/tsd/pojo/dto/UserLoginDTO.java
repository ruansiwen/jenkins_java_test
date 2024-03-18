package com.example.tsd.pojo.dto;

import com.example.tsd.annotation.CheckPhone;
import com.example.tsd.enums.PhoneModeEnum;
import com.example.tsd.group.UniqueUserGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * ClassName: UserLoginDTO
 * Package: com.example.tsd.pojo.dto
 * Descripttion:
 *
 * @Author:
 * @Create 2024/1/17 0:20
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UserLoginDTO {
    @NotBlank(message = "登录用户不能为空")
    public String username;

    // @NotNull(message = "密码可以为空串但不能为null")
    // @Min(value = 3,message = "密码最少三个字符")
    // @Max(value = 6,message = "密码最多六个字符")
    @CheckPhone(message = "自定义校验:手机号", groups = UniqueUserGroup.Crud.Create.class,value = PhoneModeEnum.IS_MOBILE)
    public String password;
    public String phoneNumber;
}
