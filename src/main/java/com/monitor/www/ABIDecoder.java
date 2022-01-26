package com.monitor.www;

import com.alibaba.fastjson.JSON;
import com.monitor.www.util.ABIHexUtil;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;

/**
 * @Description:
 * @Author: yaachou
 * @Date: 2021/9/7
 * @Version: $
 */

public class ABIDecoder {

    private List<String> types = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private List<String> results;

    public String run(String paramTypes, String paramInput) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        parseParamTypes(paramTypes);
        parseParamInput(paramInput);
        results = new ArrayList<>();
        decode();

        String result = StringUtils.join(results, ",");
        System.out.println(JSON.toJSON(JSON.toJSON(results)));

        return result;
    }


    public void parseParamTypes(String paramTypes) {
        paramTypes = paramTypes.replaceAll("[() ]", "");
        String[] data = paramTypes.split(",");
        for (String d : data) {
            types.add(d);
        }
    }

    public void parseParamInput(String input) {
        //input 处理
        if (input.contains("0x") && input.length() % 64 != 0) {
            input = input.substring(10);
        }
        //计算长度
        int num = input.length() / 64;

        for (int i = 0; i < num; i++){
            String value = input.substring(0, 64);
            values.add(value);
            System.out.println(value);
            input = input.substring(64);
        }
    }

    public void decode() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int n = types.size();
        for (int i = 0; i < n; i++) {
            String type = types.get(i);

            //动态数组
            if (type.equals("bytes") || type.equals("string") || type.matches("[\\w]+\\[\\]")) {
                parseDynamicArray(i);
                n--;
                i--;
            }

            //静态数组
            else if (type.matches("[\\w]+\\[(\\d)+\\]")) {
                parseStaticArray(i);
                i--;
                n--;
            }

            //非数组（静态或动态）
            else if (type.matches("[\\w]+")) {
                parseStaticType(i);
                n--;
                i--;
            }

            //类型中含有非法符号
            else {
                //System.out.println("未知数据类型：" + type);
                results.clear();
                return;
            }
        }
    }

    public void parseStaticType(int i) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String type = types.get(i);
        if (type.contains("[") || type.equals("bytes") || type.equals("string")) {
            return;
        }

        //uint4...256或int4...256
        if (type.contains("int")) {
            BigInteger integer = ABIHexUtil.Hex32ToUInt(values.get(i));
            results.add(integer.toString());
        }

        //bytes4...256
        else if (type.matches("bytes[\\d]+")) {
            //String bytes = ABIHexUtil.Hex32ToBytes(values.get(i));
            String bytes = values.get(i);
            results.add(trimZero(bytes));
        }

        //address.csv
        else if (type.equals("address.csv")) {
            String address = ABIHexUtil.addressToHex32(values.get(i));
            System.out.println(address);
            results.add(address);
        }

        //bool
        else if (type.contains("bool")) {
            boolean flag = ABIHexUtil.Hex32ToBool(values.get(i));
            results.add(String.valueOf(flag));

        //未知类型
        } else {
            //System.out.println("未知类型按静态数据类型处理：" + type);
            results.add(values.get(i));
        }

        types.remove(type);
        values.remove(values.get(i));
    }

    public void parseStaticArray(int i) {
        String type = types.get(i);
        int j = type.indexOf("[");
        StringBuffer buffer = new StringBuffer();
        int count = Integer.parseInt(type.substring(j+1, type.length()-1));
        for (int c = 0; c < count; c++) {
            String value = values.get(i + c);
            buffer.append(value);
            values.remove(value);
            c--;
            count--;
        }

        for (int c = 0; c < count; c++) {
            values.remove(values.get(i+c));
        }
        types.remove(type);
        results.add(String.valueOf(buffer));
    }

    public void parseDynamicArray(int i) {
        values.remove(i);
        String type = types.get(i);
        int offset = types.size() - 1;
        int count = ABIHexUtil.Hex32ToInteger(values.get(offset));
        values.remove(values.get(offset));
        StringBuffer buffer = new StringBuffer();
        if (type.equals("bytes") || types.equals("string")) {
            for (int j = 0; j < values.size(); j++) {
                String value = values.get(offset + j);
                if (value.endsWith("000")) {
                    value = trimZero(value);
                    buffer.append(value);
                    values.remove(value);
                    break;
                } else {
                    buffer.append(value);
                    values.remove(value);
                    j--;
                }
            }
        } else {
            for (int j = 0; j < count; j++) {
                String value = values.get(offset + j);
                buffer.append(value);
                values.remove(value);
                j--;
                count--;
            }
        }

        types.remove(type);
        results.add(String.valueOf(buffer));
    }

    public String trimZero(String value) {
        int i;
        for (i = value.length(); i > 0; i--) {
            if (value.charAt(i-1) == '0') {
                continue;
            } else {
                break;
            }
        }
        value = value.substring(0, i);
        return value;
    }
}
