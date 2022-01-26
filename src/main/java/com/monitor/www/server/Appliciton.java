package com.monitor.www.server;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.monitor.www.pojo.MethodSigin;
import com.monitor.www.tools.Decoder;
import com.monitor.www.tools.ErcAbiEnum;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@Component
public class Appliciton implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
//        初始化map
        read();
        Decoder.init();
        log.info("启动中");
        new Fitter().Enever();
    }
    @PreDestroy
    public  void destory() {
        System.out.println("我被销毁了、、、、、我是用的@PreDestory的方式、、、、、、");
    }
    public static void read(){

        String filePath = "holders.csv";

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath);
            // 读表头
            csvReader.readHeaders();
            Map<String, MethodSigin> maPoco =new HashMap<>();
            while (csvReader.readRecord()){
                MethodSigin methodSigin =new MethodSigin();
                // 读一整行
                // 读这行的某一列
                String id = csvReader.get("address");
                String name = csvReader.get("name");
                methodSigin.setId(id);
                methodSigin.setName(name);
            }
            Decoder.maPoco=maPoco;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
