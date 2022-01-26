package com.monitor.www.tools;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.monitor.www.pojo.MethodSigin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
     /**
     * @desc: 读取csv文件
     * @time: 2019年7月26日 22:50
     */
    public class export_zhong {
         public static void main(String[] args) {
             read();
         }
         public static void read(){

             String filePath = "demo.csv";

             try {
                 // 创建CSV读对象
                 CsvReader csvReader = new CsvReader(filePath);
                 // 读表头
                 csvReader.readHeaders();
                 List<MethodSigin> methodSigin1=new ArrayList<>();
                 while (csvReader.readRecord()){
                     MethodSigin methodSigin =new MethodSigin();
                     // 读一整行
                     // 读这行的某一列
                     String id = csvReader.get("id");
                     String name = csvReader.get("name");
                     String methnd = csvReader.get("methnd");
                     methodSigin.setErcAbiEnum(methnd);
                     methodSigin.setId(id);
                     methodSigin.setName(name);
                     methodSigin1.add(methodSigin);
                     System.out.println(                     ErcAbiEnum.valueOf(id).paramTypes);
                 }


             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

     }

