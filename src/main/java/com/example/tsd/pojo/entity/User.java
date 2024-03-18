package com.example.tsd.pojo.entity;

import com.example.tsd.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * ClassName: User
 * Package: com.example.tsd.pojo.entity
 * Descripttion:
 *
 * @Author:
 * @Create 2024/1/17 0:20
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    @Excel(name = "姓名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @Excel(name = "密码")
    private String password;
    @Excel(name = "邮箱")
    private String email;

    @Excel(name = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String male;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;
    private String modifiedBy;
    private String creator;
}