package com.example.tsd.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.example.tsd.enums.ResultEnum;
import com.example.tsd.mapper.UserMapper;
import com.example.tsd.pojo.entity.User;
import com.example.tsd.service.UserService;
import com.example.tsd.utils.BaseResult;
import com.example.tsd.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: UserServiceImpl
 * Package: com.example.tsd.service.impl
 * Descripttion:
 *
 * @Author:
 * @Create 2023/11/15 23:07
 * @Version 1.0
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UserMapper userMapper;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void userRegister(User user) {
        userMapper.userRegister(user);
    }

    @Override
    public User getUserByPhoneOrName(String phoneNumber) {
        return userMapper.getUserByPhoneOrName(phoneNumber);
    }

    @Override
    public List<User> getUserBy(User user) {
        return userMapper.getUserBy(user);
    }

    @Override
    public BaseResult sendCode(String phone, HttpSession httpSession) {
        String code = RandomUtil.randomString(4);
        // redis操作
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set("code",code);

        // 保存到session
        httpSession.setAttribute("code",code);
        // TODO:发送短信
        return BaseResult.success(ResultEnum.SUCCESS);
    }

    /**
     * 手机号登录
     * @param phone
     * @return
     */
    @Override
    public BaseResult userLoginByPhone(String phone) {
        Long userId = null;
        User user = userMapper.getUserByPhoneOrName(phone);
        if(user==null){
            User newUser = new User();
            newUser.setUsername(phone);
            newUser.setPhoneNumber(phone);
            newUser.setPassword(phone);
            userId = userMapper.userRegister(newUser);
        }else {
            userId = user.getId();
        }
        ThreadLocalUtil.set(userId);

        return BaseResult.success(ResultEnum.SUCCESS);
    }


}
