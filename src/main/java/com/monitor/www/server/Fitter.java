package com.monitor.www.server;

import com.monitor.www.ABIDecoder;
import com.monitor.www.tools.Decoder;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.lang.reflect.InvocationTargetException;


@Service
@Log
public class Fitter {
    public void Enever(){
        Web3j web3=Web3j.build(new HttpService("https://bsc.getblock.io/mainnet/?api_key=b14b9163-184f-458e-81f0-25e18efdc8a3"));
        web3.pendingTransactionFlowable().subscribe(txs->{
            if (Decoder.maPoco.get(txs.getFrom())!=null) {
                if (txs.getInput().length()>=10){
                String input = txs.getInput().substring(0, 10);
                String value = Decoder.map.get(input);
                if (value == null) {
                    value = "未知方法";
                }
                String text = String.format(
                        "发送方:" + txs.getFrom()+ "          "+Decoder.maPoco.get(txs.getFrom()).getName()+"\n\n"
                        + "接收方:" + txs.getTo() + "\n\n"
                        + "发送数量        " + txs.getValue() + "\n\n"
                        + "操作方式         " + value + "\n\n"
                        + "[hash地址](https://bscscan.com/tx/" + txs.getHash() + ")\n\n"
                        + "[钱包地址](https://bscscan.com/address/" + txs.getFrom() + ")\n\n"
                );
                    synchronized (this) {
                        Decoder.markdownMessage(text);
                        }
                }
            }
        });
//        web3.transactionFlowable().subscribe(tx->{
//            if (tx.getTo().equalsIgnoreCase("0x10ed43c718714eb63d5aa57b78b54704e256024e")) {
//                String input = tx.getInput().substring(0, 10);
//                String value = Decoder.map.get(input);
//                if (value == null) {
//                    value = "未知方法";
//                }
//                String text = String.format(
//                        "发送方:" + tx.getFrom()+ "\n\n"
//                        + "接收方:" + tx.getTo() + "\n\n"
//                        + "发送数量        " + tx.getValue() + "\n\n"
//                        + "操作方式         " + value + "\n\n"
//                        + "[hash地址](https://bscscan.com/tx/" + tx.getHash() + ")\n\n"
//                        + "[钱包地址](https://bscscan.com/address/" + tx.getFrom() + ")\n\n"
//                );
//                synchronized (this) {
//                    Decoder.markdownMessage(text);
//                    //销毁方法
//                    System.exit(1);
//                    new Appliciton().destory();
//                }
//            }
//        });
    }
    @Async

     void Asyncing(String input, String methodid) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ABIDecoder decoder = new ABIDecoder();
        String paramInput = input;



    }


}
