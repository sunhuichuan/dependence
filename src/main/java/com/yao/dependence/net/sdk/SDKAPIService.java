package com.yao.dependence.net.sdk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SDKAPIService {
    /**
     * app的host
     */
    String WEIXIN_HOST = "https://api.weixin.qq.com/";


    interface WeixinAPI{

        //以下三个接口的 scope 是 snsapi_base

        //获取微信Access_Token
        String ACCESS_TOKEN = "sns/oauth2/access_token";
        //刷新或续期access_token使用
        String REFRESH_ACCESS_TOKEN = "/sns/oauth2/refresh_token";
        //检查access_token有效性
        String CHECK_ACCESS_TOKEN = "/sns/auth";

        //下面这个接口的 scope 是 snsapi_userinfo
        //获取用户个人信息
        String USER_INFO = "/sns/userinfo";
    }



    /**
     * 获得WeixinAccessToken
     * @return
     */
    @GET(WeixinAPI.ACCESS_TOKEN)
    Call<String> getWeixinAccessToken(@Query("appid") String appid,@Query("secret") String secret,@Query("code") String code,@Query("grant_type") String grant_type);
    /**
     * 获得WeixinAccessToken
     * @return
     */
    @GET(WeixinAPI.USER_INFO)
    Call<String> getWeixinUserInfo(@Query("access_token") String access_token,@Query("openid") String openid);



}