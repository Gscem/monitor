package com.monitor.www;

import com.alibaba.fastjson.JSON;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

@SpringBootTest
class MonitorApplicationTests {

    @Test
    void contextLoads() throws UnsupportedEncodingException {
        String str = "transfer(address.csv,uint256)";

        byte[] srtbyte = str.getBytes();
        String res = new String(srtbyte);

        System.out.println(res);
        System.out.println(JSON.toJSON(srtbyte));
        System.out.println();
        byte[] k={(byte)71,(byte)87};//这是一个byte数组，为大写英文字母G、W
        System.out.print("转化为:"+new String(k));//全宇宙最简单的转化方式，有没有？

    }

}
