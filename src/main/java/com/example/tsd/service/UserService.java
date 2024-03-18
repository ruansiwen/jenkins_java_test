package com.example.tsd.service;

import com.example.tsd.pojo.dto.UserLoginDTO;
import com.example.tsd.pojo.entity.User;
import com.example.tsd.utils.BaseResult;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * ClassName: UserService
 * Package: com.example.tsd.service
 * Descripttion:
 *
 * @Author:
 * @Create 2023/11/15 23:06
 * @Version 1.0
 */
public interface UserService {
    /**
     * 根据用户id查询
     * @param id
     * @return
     */
    public User getUserById(Integer id);

    /**
     * 用户注册
     * @param user
     */
    public void userRegister(User user);

    /**
     * 根据手机号或者名字查询
     * @param phoneNumber
     * @return
     */
    public User getUserByPhoneOrName(String phoneNumber);

    /**
     *
     * @param user
     * @return
     */
    public List<User> getUserBy(User user);

    /**
     * 发短信
     * @param phone
     * @param httpSession
     * @return
     */
    BaseResult sendCode(String phone, HttpSession httpSession);


    BaseResult userLoginByPhone(String phone);
}
