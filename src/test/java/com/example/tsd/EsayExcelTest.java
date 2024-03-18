package com.example.tsd;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.ListUtils;
import com.example.tsd.listener.ExcelListener;
import com.example.tsd.pojo.demo.DemoData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class EsayExcelTest {


    @Test
    public void simpleRead() {
        // String fileName = "C:\\Users\\admin\\Desktop\\测试.xlsx";
        String fileName = "C:\\Users\\dell\\Desktop\\测试.xlsx";
        readFile1(fileName);
    }


    public void readFile1(String fileName){
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<DemoData> userInfoList =
                EasyExcel.read(fileName,DemoData.class,new ExcelListener()).head(DemoData.class).sheet(2).doReadSync();
        System.out.println("总数 = " + userInfoList.size());
        // 过滤username为空的信息，并分组为 map<列名，列数据的list>
        Map<String, List<DemoData>> listMap =
                userInfoList.stream()
                        .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getEmployeeId()))
                        .collect(Collectors.groupingBy(DemoData::getEmployeeId));
        for (Map.Entry<String, List<DemoData>> stringListEntry : listMap.entrySet()) {
            if (stringListEntry.getValue().size() >= 1) {
                System.out.println("username = " + stringListEntry.getKey());
            }
        }
    }

    // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    public void readFile2(String fileName){
        // 匿名内部类 不用额外写一个DemoDataListener
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new ReadListener<DemoData>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;
            /**
             *临时存储
             */
            private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(DemoData data, AnalysisContext context) {
                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", cachedDataList.size());
                log.info("存储数据库成功！");
            }
        }).sheet(2).doRead();
    }


    public void readFile3(){
        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
        // EasyExcel.read(fileName, DemoData.class, new PageReadListener<DemoData>(dataList -> {
        //     for (DemoData demoData : dataList) {
        //         log.info("读取到一条数据{}", JSON.toJSONString(demoData));
        //     }
        // })).sheet().doRead();
    }

    public void readFile4(String fileName){
        // 一个文件一个reader
        try (ExcelReader excelReader = EasyExcel.read(fileName, DemoData.class, new ExcelListener()).build()) {
            // 构建一个sheet 这里可以指定名字或者no
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // 读取一个sheet
            excelReader.read(readSheet);
        }
    }
}
