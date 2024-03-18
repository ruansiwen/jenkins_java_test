package com.example.tsd.controller;


import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.example.tsd.enums.ResultEnum;
import com.example.tsd.exception.FileException;
import com.example.tsd.pojo.entity.User;
import com.example.tsd.pojo.valueConfig.DemoConfig;
import com.example.tsd.pojo.valueConfig.FileConfig;
import com.example.tsd.utils.BaseResult;
import com.example.tsd.utils.FileUtil;
import com.example.tsd.utils.poi.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@Slf4j
@Tag(name = "测试")
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    public FileConfig fileConfig;

    /**
     * 上传文件保存到classpath
     *
     * @param files
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFile")
    @Operation(summary = "上传文件")
    public BaseResult uploadFile(@RequestParam("files") MultipartFile[] files) throws IOException {
        log.info("Uploading file：{}", files);
        ArrayList<Object> list = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            // System.out.println("original" + originalFilename);
            String fileUrl = FileUtil.saveToVClassPath(fileConfig.getDirectory(), file);
            list.add(fileUrl);
        }
        return BaseResult.success(ResultEnum.SUCCESS, list);
    }

    @PostMapping("/chunkUpload")
    @Operation(summary = "分片上传")
    public synchronized BaseResult chunkUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename,
            @RequestParam("chunkName") String chunkName,
            @RequestParam("chunkIndex") String chunkIndex) throws IOException {
        // System.out.println(file.getBytes());
        // System.out.println(filename);
        // System.out.println(chunkName);
        // System.out.println(chunkSize);

        String chunkFileFolder = fileConfig.getChunkFile();

        boolean isUpload = FileUtil.saveChunkToVClassPath(chunkFileFolder, file, chunkIndex, filename);
        if (isUpload) {
            return BaseResult.success(ResultEnum.SUCCESS);
        } else {
            return BaseResult.fail(500, "有重复文件");
        }
    }

    @PostMapping("/mergeFile")
    @Operation(summary = "合并文件")
    public synchronized BaseResult mergeFile(@RequestParam String fileName) {
        String chunkFileFolder = fileConfig.getChunkFile();
        boolean ismerge = FileUtil.mergeChunkFile(chunkFileFolder, fileName);
        if (ismerge) {
            return BaseResult.success(ResultEnum.SUCCESS);
        } else {
            return BaseResult.fail(500, "合成失败");
        }
    }

    @PostMapping("/test")
    @Operation(summary = "测试接口")
    public BaseResult uploadFile2(@RequestBody Map<String, Integer> payload) {
        // System.out.println("测试" + demoConfig.getDirectory());

        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("file", payload);

        return BaseResult.success(ResultEnum.SUCCESS, objectObjectHashMap);
    }

    @GetMapping("/testGetPath")
    @Operation(summary = "测试获取路径")
    public void getPath() throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:").getPath();
        String path2 = this.getClass().getClassLoader().getResource("").getPath();
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        System.out.println(path2);
        System.out.println(path);
        System.out.println(applicationHome);
    }


    /**
     * 导入数据
     */
    @PostMapping("/importData")
    @Operation(summary = "导入数据")
    public BaseResult importData(MultipartFile file, boolean updateSupport) throws Exception {

        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        List<User> userList = util.importExcel(file.getInputStream());

        return BaseResult.success(ResultEnum.SUCCESS);
    }


    /**
     * 导出excel
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List userList = new ArrayList<>();
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("id", 1);
        hashMap.put("username", "张三");
        hashMap.put("password", "2273357202@qq.com");
        userList.add(hashMap);
        EasyExcel.write(response.getOutputStream(), User.class).sheet("模板").doWrite(userList);
    }
}
