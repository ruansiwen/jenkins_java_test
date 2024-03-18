package com.example.tsd.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.example.tsd.annotation.PassToken;
import com.example.tsd.enums.ResultEnum;
import com.example.tsd.group.UniqueUserGroup;
import com.example.tsd.pojo.dto.UserLoginDTO;
import com.example.tsd.pojo.entity.User;
import com.example.tsd.service.UserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import com.example.tsd.utils.BaseResult;
import com.example.tsd.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * ClassName: UserController
 * Package: com.example.tsd.controller
 * Descripttion:
 *
 * @Author:
 * @Create 2023/11/15 22:54
 * @Version 1.0
 */

@RestController
@RequestMapping("user")
@Slf4j
@Validated
@Tag(name = "用户接口")
public class UserController {
    //`1.创建日志对象
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserService userService;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResult userRegister(@RequestBody User user) {

        User userByPhone = userService.getUserByPhoneOrName(user.getPhoneNumber());
        if (userByPhone != null) {
            return BaseResult.fail(HttpStatus.BAD_REQUEST.value(),"该手机号已注册");
        }
        userService.userRegister(user);

        return BaseResult.success(ResultEnum.SUCCESS);
    }

    @Operation(summary = "手机号登录")
    @PostMapping("/loginByPhone")
    public BaseResult userLoginByPhone(
            @RequestParam("phoneNumber") @Validated @Min(3) String phone,
            @RequestParam("code") String code) {
        if (phone == null) {
            return BaseResult.fail(500, "手机号为空");
        }
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String code1 = stringStringValueOperations.get("code");
        if (code != code1) {
            return BaseResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "验证码错误");
        }
        return userService.userLoginByPhone(phone);
    }

    @Operation(summary = "账号密码登录")
    @PostMapping("/loginByPassword")
    public BaseResult userLoginByPassword(@RequestBody @Validated(value = UniqueUserGroup.Crud.Create.class)  UserLoginDTO user) throws InterruptedException {
        log.info("user:{}", user);
        User userBy = null;
        if (user.getUsername() != null) {
            userBy = userService.getUserByPhoneOrName(user.getUsername());
        }
        if (userBy == null) {
            return BaseResult.fail(HttpStatus.BAD_REQUEST.value(),"用户账号或密码错误");
        }
        if (!user.getPassword().equals(userBy.getPassword())) {
            return BaseResult.fail(HttpStatus.BAD_REQUEST.value(),"用户账号或密码错误");
        }

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("user", userBy.getId());
        String token = JwtUtil.generateToken(JSONUtil.parseObj(hashMap));
        log.info("颁发token:{}", token);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return BaseResult.success(ResultEnum.SUCCESS,map);

    }


    @PostMapping("/code")
    @PassToken(required = true)
    @Operation(summary = "发送手机验证码")
    public BaseResult sendCode(@RequestParam("phoneNumber") String phone, boolean code, HttpSession httpSession) {
        log.info("phtoneNumber：{},{}", phone, code);
        // 1，TODO：验证手机号
        boolean isMobile = Validator.isMobile(phone);
        if (!isMobile) {
            return BaseResult.fail(HttpStatus.BAD_REQUEST.value(), "请输入正确的手机号");
        }
        return userService.sendCode(phone, httpSession);
    }

    @PassToken(required = false)
    @GetMapping("{id}")
    @Operation(summary = "根据id查询用户")
    public User getUserById(@PathVariable Integer id) {
        // try {
        // } catch (Exception e) {
        //     logger.error("e",e);
        //     throw new LoginException(500,"登录错误");
        // }
        // logger.info("获取用户的信息：",id);

        //     long i = 0;
        //     long a = 3/i;
        User user = userService.getUserById(id);

        log.info("user:{}", user);


        return user;
    }
}
