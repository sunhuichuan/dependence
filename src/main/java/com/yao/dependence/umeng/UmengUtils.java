package com.yao.dependence.umeng;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.AnalyticsConfig;

/**
 *
 * Created by huichuan on 16/9/3.
 */
public class UmengUtils {
    private static UmengUtils ourInstance = new UmengUtils();

    public static UmengUtils getInstance() {
        return ourInstance;
    }

    private UmengUtils() {
    }

    //友盟AppKey
    public static final String APP_KEY = "57cac7b967e58eb84a004dd4";

    /**
     * 获取Umeng需要的设备id
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 初始化友盟的key和channel
     */
    public static void initUmengKeyChannel(){
        String channelValue = "main_channel";

        AnalyticsConfig.setAppkey(APP_KEY);
        AnalyticsConfig.setChannel(channelValue);
    }




}
