package com.yao.dependence.net.sdk;

import com.yao.dependence.event.WeiXinLoginEvent;

import retrofit2.Call;

/**
 * 第三方sdk需要的网络请求管理类
 * Created by huichuan on 16/5/28.
 */
public class SDKRequestManager {
    private static SDKRequestManager ourInstance = new SDKRequestManager();

    public static SDKRequestManager getInstance() {
        return ourInstance;
    }

    SDKAPIService mRequestService;

    private SDKRequestManager() {
        SDKHttpClient<SDKAPIService> request = SDKHttpClient.getInstance(SDKAPIService.WEIXIN_HOST, SDKAPIService.class);
        mRequestService = request.getRequestService();
    }


    /**
     * 获取微信AccessToken
     */
    public Call<String> getWeixinAccessToken(String appId, String secret, WeiXinLoginEvent event) {
        return mRequestService.getWeixinAccessToken(appId, secret, event.code, "authorization_code");
    }
    /**
     * 获取微信userInfo
     */
    public Call<String> getWeixinUserInfo(String accessToken, String openId) {
        return mRequestService.getWeixinUserInfo(accessToken, openId);
    }


}
