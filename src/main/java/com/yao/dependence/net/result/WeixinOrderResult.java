package com.yao.dependence.net.result;

import com.google.gson.annotations.SerializedName;

/**
 * 生成微信支付订单
 * Created by huichuan on 16/6/11.
 */
public class WeixinOrderResult {

    public String appid;//":"wx097496417c96d56b",
    public String mch_id;//":"1348243101",
    public String nonce_str;//":"Y0b13ryTZlSg6Zh0",
    public String sign;//":"7C3DCB3F915F642B215DD6C4283C0576",
    public String prepay_id;//":"wx201606081837430e84138e750171414037",
    public String trade_type;//":"APP"
    public String timestamp;//": 1466937530,
    @SerializedName("package")
    public String packageValue;//": "Sign=WXPay",


    @Override
    public String toString() {
        return "WeixinOrderResult{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", timestamp=" + timestamp +
                ", packageValue='" + packageValue + '\'' +
                '}';
    }
}
