package com.example.tsd;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.example.tsd.controller.DemoController;
import com.example.tsd.enums.GenderEnum;
import com.example.tsd.mapper.UserMapper;
import com.example.tsd.pojo.demo.DemoData;
import com.example.tsd.pojo.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
class TsdApplicationTests {

    @Autowired
    public UserMapper userMapper;

    @Autowired
    public DemoController demoController;

    @Test
    void contextLoads() {

    }

    @Test
    void getPath() throws FileNotFoundException {
        String path = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(path);
    }

    @Test
    void covert(){
        GenderEnum convert = GenderEnum.convertByStream(2);
        System.out.println(convert.getDescription());
    }


    @Test
    void BeanUti(){
        Class<?> propertyType = BeanUtils.findPropertyType(GenderEnum.MALE.getDescription(), GenderEnum.class);
        System.out.println(propertyType);
    }






}
