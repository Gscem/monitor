package com.monitor.www.tools;


import java.util.List;

/**
 * 处理erc input
 */
public enum ErcAbiEnum {
    SIMPLEA("address.csv"),
    SIMPLEU("uint256"),
    SIMPLEUU("uint256,uint256,uint256"),
    COMPLEX("address.csv,address.csv,uint256"),
    COMPLEXA("address.csv,uint256"),
    COMPLEXB("uint256,address.csv[],address.csv,uint256"),
    COMPLEXC("uint256,uint256,address.csv[],address.csv,uint256"),
    COMPLEXD("address.csv,uint256,uint256,uint256,uint256,address.csv"),
    SATURDAY("address.csv,uint256,address.csv[]"),
    SATURDAYS("uint256,address.csv[]"),
    SUNDAY("address.csv,address.csv,uint256");//记住要用分号结束
    ErcAbiEnum(String desc){
        this.paramTypes=desc;
    }
    public String paramTypes;
}
