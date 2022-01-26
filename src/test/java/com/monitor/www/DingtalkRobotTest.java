package com.monitor.www;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.monitor.www.keccak.FIPS202;
import com.monitor.www.keccak.KeccakSponge;
import com.monitor.www.tools.Decoder;
import com.taobao.api.ApiException;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

/**
 * <p> 钉钉机器自定义机器人测试 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2019/7/24 21:44
 */
public class DingtalkRobotTest {
    @Test
    void demo() throws IOException {
  
    }
    //发送普通文本消息
    @Test
    public void textMessage() throws ApiException {
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
        System.out.println(response.getErrcode());
        System.out.println(response.getErrmsg());
    }

    //发送link消息
    @Test
    public void linkMessage() throws ApiException {
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
        System.out.println(response.getErrcode());
        System.out.println(response.getErrmsg());
    }

    //发送markdown消息
    @Test
    public void markdownMessage() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=3457b7f93dce57b63627330e567662575a34a200c1c572db6eb206bfd7d280bf");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("预警");
//        "链接 :\n" +
//                "[this is a link](http://name.com)"
        markdown.setText("1231343");
        request.setMarkdown(markdown);
        OapiRobotSendResponse response = client.execute(request);
        System.out.println(response.getErrcode());
        System.out.println(response.getErrmsg());
    }





}
