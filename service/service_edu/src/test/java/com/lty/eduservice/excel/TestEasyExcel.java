package com.lty.eduservice.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    @Test
    public  void easyExcelTest(){
        String fileName = "E:\\write.xlsx"; //生成Excel存储位置

        EasyExcel.write(fileName,DataDemo.class).sheet("学生列表").doWrite(getData());
    }

    public static List<DataDemo> getData(){
        List<DataDemo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DataDemo data = new DataDemo();
            data.setSno(i);
            data.setSname("lisi"+i);
            list.add(data);
        }
        return list;
    }
    //实现读操作
    @Test
    public void easyExcelTest2(){
        String fileName = "E:\\write.xlsx"; //生成Excel存储位置
        EasyExcel.read(fileName,DataDemo.class,new ExcelListener()).sheet().doRead();
    }

}
