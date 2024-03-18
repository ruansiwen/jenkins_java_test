package com.example.tsd.utils.esayExcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.DefaultConverterLoader;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.example.tsd.enums.GenderEnum;
import com.example.tsd.pojo.demo.DemoData;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class CustomNumberConverter implements Converter<GenderEnum> {

    // private final DefaultConverterLoader defaultConverterLoader = new ReadConverterContext<GenderEnum>();
    @Override
    public Class<?> supportJavaTypeKey() {
        // 返回要转换的Java类型
        return GenderEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // 返回Excel中对应的数据类型，通常是NUMBER
        return CellDataTypeEnum.NUMBER;
    }

    /**
     * 将excel单元格数据转换成对象属性值（适配多个逗号分隔的字典值）
     *
     * @param cellData             单元格的数据
     * @param contentProperty excel每一行的数据内容
     * @param globalConfiguration  global全局配置
     * @return 转换后的对象属性值
     * @throws Exception 异常
     */
    @Override
    public GenderEnum convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        System.out.println("cellData:"+cellData);
        System.out.println("contentProperty:"+globalConfiguration);
        System.out.println("globalConfiguration:"+globalConfiguration);
        System.out.println(cellData.getStringValue());
        System.out.println(cellData.toString());


        Field field = contentProperty.getField();

        System.out.println(field.getName());
        System.out.println(field.getType());


        return Converter.super.convertToJavaData(cellData, contentProperty, globalConfiguration);
    }

    /**
     * 将对象属性值转换成excel单元格数据（适配多个逗号分隔的字典值）
     *
     * @param value            对象的属性值
     * @param contentProperty excel每一行的数据内容
     * @param globalConfiguration  global全局配置
     * @return 转换后的对象属性值
     * @throws Exception 异常
     */
    @Override
    public WriteCellData<String> convertToExcelData(GenderEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        System.out.println("2");
        return (WriteCellData<String>) Converter.super.convertToExcelData(value, contentProperty, globalConfiguration);
    }



}
