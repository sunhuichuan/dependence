package com.yao.dependence.share.event;

/**
 * 支付event
 * Created by huichuan on 16/7/10.
 */
public class PayEvent {

    /**微信支付*/
    public static final int PAY_TYPE_WEIXIN = 1;
    /**支付宝支付*/
    public static final int PAY_TYPE_ALIPAY = 2;


    public static final int ERROR_SUCCESS = 1;
    public static final int ERROR_FAILED = 2;
    public static final int ERROR_CANCEL = 3;


    int payType;
    int errorCode;

    public PayEvent(int payType,int errorCode){
        this.payType = payType;
        this.errorCode = errorCode;
    }


    public boolean isWeixin(){
        return payType == PAY_TYPE_WEIXIN;
    }


    public boolean isAlipay(){
        return payType == PAY_TYPE_ALIPAY;
    }


    public boolean isPaySuccess(){
        return errorCode == ERROR_SUCCESS;
    }

    public boolean isPayFailed(){
        return errorCode == ERROR_FAILED;
    }

    public boolean isPayCancel(){
        return errorCode == ERROR_CANCEL;
    }



}
