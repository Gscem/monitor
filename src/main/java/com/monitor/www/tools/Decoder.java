package com.monitor.www.tools;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.monitor.www.pojo.MethodSigin;
import com.taobao.api.ApiException;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class Decoder {
    public static Map<String,String> map=new HashMap<>();
    public static String RPC_API1="https://bsc-dataseed1.ninicoin.io";
    public static String RPC_API="https://bsc.getblock.io/mainnet/?api_key=b14b9163-184f-458e-81f0-25e18efdc8a3";
    public static Map<String, MethodSigin> maPoco;
   public static void init(){
       //读取csv 文件进行解析
       map.put("0xf2c298be","register");
       map.put("0x095ea7b3","approve");
       map.put("0x23b872dd","transferFrom");
       map.put("0x79ce9fac","transfer");
       map.put("0x379607f5","claim");
       map.put("0xa7154d22","cancel");
       map.put("0xe5fe4f31","buy");
       map.put("0xa9059cbb","transfer");
       map.put("0x","transfer");
       map.put("0x5c9f8aa8","buyTokenBNB");
       map.put("0x7ff36ab5","swapExactETHForTokens");
       map.put("0xfb3bdb41","swapETHForExactTokens");
       map.put("0x791ac947","swapExactTokensForETHSupportingFeeOnTransferTokens");
       map.put("0x18cbafe5","swapExactTokensForETH");
       map.put("0xd7bb99ba","contribute");
       map.put("0x9b972092","fight");
       map.put("0xd96a094a","buy");
       map.put("0xfaf039f4","mintCharacter");
       map.put("0x5f575529","swap");
       map.put("0xed9999ca","changeListingPrice");
       map.put("0xf6d36ccb","addListing");
       map.put("0xb2ddee06","cancelListing");
       map.put("0xa7ff3b23","bid");
       map.put("0x6ba4c138","claim");
       map.put("0xaa6b873a","betBear");
       map.put("0x57fb096f","betBull");
       map.put("0xc73a2d60","disperseToken");
       map.put("0xab834bab","atomicMatch_");
       map.put("0xf6fff9bd","Bulksend Token");
       map.put("0x40c10f19","mint");
       map.put("0xe8e33700","addLiquidity");
       map.put("0xded9382a","removeLiquidityETHWithPermit");
       map.put("0xf305d719","addLiquidityETH");
       map.put("0x60de1a9b","Look");
       map.put("0x0b66f3f5","multisendToken");
       map.put("0xae7b0333","executeOrder");
       map.put("0x8803dbee","swapTokensForExactTokens");
       map.put("0x4a25d94a","swapTokensForExactETH");
       map.put("0x5c11d795","swapExactTokensForTokensSupportingFeeOnTransferTokens");
       map.put("0x38ed1739","swapExactTokensForTokens");
       map.put("0xb6f9de95","swapExactETHForTokensSupportingFeeOnTransferTokens");
       map.put("0x2195995c","removeLiquidityWithPermit");
       map.put("0x5b0d5984","removeLiquidityETHWithPermitSupportingFeeOnTransferTokens");
       map.put("0xaf2979eb","removeLiquidityETHSupportingFeeOnTransferTokens");
       map.put("0x02751cec","removeLiquidityETH");
       map.put("0xbaa2abde","removeLiquidity");
       map.put("0xad615dec","quote");
       map.put("0xd06ca61f","getAmountsOut");
       map.put("0x1f00ca74","getAmountsIn");
       map.put("0x054d50d4","getAmountOut");
       map.put("0x85f8c259","getAmountIn");
       map.put("0xc45a0155","factory");
       map.put("0xad5c4648","Weth");
   }

    public static void markdownMessage(String text) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=3457b7f93dce57b63627330e567662575a34a200c1c572db6eb206bfd7d280bf");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("交易预警");
//        "链接 :\n" +
//                "[this is a link](http://name.com)"
        markdown.setText(text);
        request.setMarkdown(markdown);
        OapiRobotSendResponse response = client.execute(request);
    }


    public static void textMessage() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=3457b7f93dce57b63627330e567662575a34a200c1c572db6eb206bfd7d280bf");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("\"链接\\n\" +\n" +
                "                        \"[this is a link](http://name.com)\"");
        request.setText(text);

        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll("true");//设置@所有的人
        request.setAt(at);
        OapiRobotSendResponse response = client.execute(request);
    }

    //发送link消息

    public static void linkMessage() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=3457b7f93dce57b63627330e567662575a34a200c1c572db6eb206bfd7d280bf");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl("https://www.dingtalk.com/");
        link.setPicUrl("");
        link.setTitle("时代的火车向前开");
        link.setText("这个即将发布的新版本，创始人陈航（花名“无招”）称它为“红树林”。\n" +
                "而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是“红树林");
        request.setLink(link);

        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList("13672384534"));//设置@那些人，使用的是手机号
        request.setAt(at);
        OapiRobotSendResponse response = client.execute(request);
    }
}
