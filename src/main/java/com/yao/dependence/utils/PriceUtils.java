package com.yao.dependence.utils;

import com.yao.devsdk.log.LogUtil;

/**
 *
 * Created by huichuan on 16/7/3.
 */
public class PriceUtils {


    private static final String TAG = "";

    /**
     * 把服务器返回的价格，需要显示的字符串
     * @return
     */
    public static String convertServerPriceString(String priceServiceUnit){
        float serverPrice = convertServerPrice(priceServiceUnit);
        return "￥ "+serverPrice;
    }
    /**
     * 把服务器返回的价格，转换为元为单位
     * @return
     */
    public static float convertServerPrice(String priceServiceUnit){
        try {
            float price = Float.parseFloat(priceServiceUnit) / 100;
            return price;
        }catch (Exception e){
            LogUtil.e(TAG, "服务器返回单价异常",e);
            //当服务器返回数据错误时，页面显示一个极大的数字，以免价格过低用户疯狂下单
            return 99999;
        }
    }

    /**
     * 把用户输入的价格，转换为元为单位
     * @return
     */
    public static int convertInputPrice(String priceInput){
        try {
            float price = Float.parseFloat(priceInput) * 100;
            return (int)price;
        }catch (Exception e){
            LogUtil.e(TAG, "服务器返回单价异常",e);
            //当服务器返回数据错误时，页面显示一个极大的数字，以免价格过低用户疯狂下单
            return 99999;
        }
    }

}
