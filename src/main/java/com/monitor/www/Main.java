package com.monitor.www;

import org.web3j.utils.Convert;

import java.lang.reflect.InvocationTargetException;

/**
 * @Description: 参数解码执行入口
 * @Author: yaachou
 * @Date: 2021/9/9
 * @Version: $
 */

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ABIDecoder decoder = new ABIDecoder();
//        String paramTypes = "bytes, bytes32, uint256";
//        String paramTypes = "uint256,uint256,address.csv[],address.csv,uint256";
//        String paramInput = "0x5c11d79500000000000000000000000000000000000000000000003b664b6923e9b18258000000000000000000000000000000000000000000000368ebb6da49fed7586900000000000000000000000000000000000000000000000000000000000000a00000000000000000000000009aa574a1193f49a0d221dac58ad241e48c6b287a0000000000000000000000000000000000000000000000000000000061bcc6eb0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000e9e7cea3dedca5984780bafc599bd69add087d5600000000000000000000000036953b5ec00a13edceceb3af258d034913d2a79d";
        System.out.println(Convert.fromWei(String.valueOf(5790), Convert.Unit.WEI));
//        String result = decoder.run(paramTypes, paramInput);
    }
}

