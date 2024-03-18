package com.example.tsd.mapper;

import com.example.tsd.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: UserMapper
 * Package: com.example.tsd.mapper
 * Descripttion:
 *
 * @Author:
 * @Create 2023/11/15 23:07
 * @Version 1.0
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    public User getUserById(Integer id);

    public Long userRegister(User user);

    public User getUserByPhoneOrName(String phoneNumber);

    List<User> getUserBy(User user);
}
