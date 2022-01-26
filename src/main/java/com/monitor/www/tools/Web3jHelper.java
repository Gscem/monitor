package com.monitor.www.tools;

import com.alibaba.fastjson.JSON;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.naming.ldap.ControlFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.web3j.utils.Convert.Unit.ETHER;
import static org.web3j.utils.Convert.Unit.WEI;

public class Web3jHelper {
    //空地址
    private static String EMPTY_ADDRESS = "0x0000000000000000000000000000000000000000";
    // swap 地址
//    private static String pancakeRouter="0x"+Environments.SWAP_ADDRESS.substring(26);
    private static String pancakeRouter="0x";

    /**
    * @Description: Web3jHelper
    * @Param: [url]
    * @return:  构造函数
    * @Author: em
    * @Date: 1:16 2021/12/17
    */
    /**
    * @Description: release
    * @Param: []
    * @return: void
    * @Author: em 关闭
    * @Date: 1:17 2021/12/17
    */

    public Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash,Web3j web3j) throws Exception {
        EthGetTransactionReceipt transactionReceipt =
                web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }
    /**
    * @Description: getNonce
    * @Param: [fromAddress]
    * @return: java.math.BigInteger
    * @Author: em
    * @Date: 1:16 2021/12/17
    */
    public static BigInteger getNonce(String fromAddress, Web3j web3j) {

        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ethGetTransactionCount == null) {
            return BigInteger.valueOf(-1);
        }
        nonce = ethGetTransactionCount.getTransactionCount();
        return nonce;

    }
    /**
    * @Description: getBalance
    * @Param: [address.csv]
    * @return: java.math.BigInteger
    * @Author: em
    * @Date: 1:16 2021/12/17
    */
    public BigInteger getBalance(String address,Web3j web3j) {

        BigInteger balance = null;

        try {
            EthGetBalance egb = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
            balance = egb.getBalance();
        } catch (IOException e) {
            balance = BigInteger.valueOf(0);
        }
        return balance;
    }
    /**
    * @Description: getTokenBalance
    * @Param: [contractAddress, fromAddress]
    * @return: java.math.BigInteger
    * @Author: em
    * @Date: 1:16 2021/12/17
    */
    public static String getTokenBalance(String contractAddress, String fromAddress, Web3j web3j) {

        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };

        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.PENDING).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Convert.fromWei(balanceValue.toString(),ETHER).toString();
    }
        /**
        * @Description: sendToken
        * @Param: [id, credentials, nonce, chainId, _gasPrice, contractAddress, toAddress, amount, decimals]
        * @return: java.lang.String
        * @Author: em 签名交易
        * @Date: 1:17 2021/12/17
        */
        public String sendToken(int id, Credentials credentials, BigInteger nonce, byte chainId, int _gasPrice,
                            String contractAddress, String toAddress, double amount, int decimals,Web3j web3j) {

        BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(_gasPrice*3), Convert.Unit.GWEI).toBigInteger();
        BigInteger value = BigInteger.ZERO;
        //token转账参数
        String methodName = "transfer";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address tAddress = new Address(toAddress);
        Uint256 tokenValue = new Uint256(BigDecimal.valueOf(amount).multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);

        String signedData;
        try {

            signedData = signTransaction(nonce, gasPrice, BigInteger.valueOf(100000), contractAddress, value, data, chainId, credentials,web3j);

            if (signedData != null) {
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
                String txHash = ethSendTransaction.getTransactionHash();

                return txHash;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param tokenInAddr    输入地址
     * @param tokenOutAddr    输出地址
     * @return                  返回价格
     */
    public static double getAmountsOut(Web3j web3j, String tokenInAddr, String tokenOutAddr) {

        String methodName = "getAmountsOut";
        String fromAddr = EMPTY_ADDRESS;
        List<Type> inputParameters = new ArrayList<Type>();
        //1个token wei
        Uint256 inAmount = new Uint256(new BigInteger("1000000000000000000"));
        Address inAddr = new Address(tokenInAddr);
        Address outAddr = new Address(tokenOutAddr);
        DynamicArray<Address> addrArr = new DynamicArray<Address>(inAddr, outAddr);
        inputParameters.add(inAmount);
        inputParameters.add(addrArr);
        List<TypeReference<?>> outputParameters = new ArrayList<TypeReference<?>>();
        TypeReference<DynamicArray<Uint256>> oa = new TypeReference<DynamicArray<Uint256>>() {
        };
        outputParameters.add(oa);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, pancakeRouter, data);

        EthCall ethCall;
        try {
            ethCall =web3j.ethCall(transaction,DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            List<BigInteger> resultArr = new ArrayList<>();
            if (results.size() > 0) {
                for (Type tt : results) {
                    DynamicArray<Uint256> da = (DynamicArray<Uint256>) tt;
                    List<Uint256> lu = da.getValue();
                    if (lu.size() > 0) {
                        for (Uint256 n : lu) {
                            resultArr.add((BigInteger) n.getValue());
                        }
                    }
                }
                BigDecimal bigDecimal = Convert.fromWei(String.valueOf(resultArr.get(1)), Convert.Unit.ETHER);
                return bigDecimal.doubleValue();
            }
//            return results.size()!=0? Integer.valueOf(String.valueOf(results.get(1).getValue())) :0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally {
            web3j.shutdown();
        }
        return 0;
    }

    /**
     * 签名交易
     */
    public static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String
            to,
                                         BigInteger value, String data, byte chainId, Credentials credentials,Web3j web3j) {
        byte[] signedMessage;
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }



    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

    }








    /**
     *   1个是合约地址另一个是  BUSD or BNB 获取流动性池 池子存在则返回 地址
     * @param web3j
     * @param SendContart
     * @param
     * @return
     */
    public static String  getPair(Web3j web3j, String SendContart,String Contart){
        String methodName = "getPair";
        String fromAddr = EMPTY_ADDRESS;
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(SendContart));
        inputParameters.add(new Address(Contart));
        Function function = new Function(methodName,
                inputParameters,
                Collections.<TypeReference<?>>emptyList());
        //接收返回值
//        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, "0xcA143Ce32Fe78f1f7019d7d551a6402fC5350c73", data);
        EthCall ethCall;
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();

//            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return ethCall.getValue()!=null? ethCall.getValue():null;
        } catch (ExecutionException |InterruptedException e) {
            web3j.shutdown();
            e.printStackTrace();
        }finally {
            web3j.shutdown();
        }
        return null;
    }



}