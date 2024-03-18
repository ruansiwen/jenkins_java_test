package com.example.tsd.pojo.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.tsd.enums.GenderEnum;
import com.example.tsd.utils.esayExcel.CustomNumberConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DemoData {
    @ExcelProperty(index = 0, value = "序号")
    private String index;

    @ExcelProperty(index = 1, value = "员工ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String employeeId;

    @ExcelProperty(index = 2, value = "工资")
    private Double doubleData;

    @ExcelProperty(index = 3, value = "年龄")
    private Integer age;

    @ExcelProperty(index = 4, value = "性别",converter = CustomNumberConverter.class)

    private GenderEnum gender;

    @ExcelProperty(index = 5, value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
}