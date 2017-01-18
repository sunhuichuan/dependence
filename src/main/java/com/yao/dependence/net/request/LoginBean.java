package com.yao.dependence.net.request;

import com.yao.dependence.net.APIService;


/**
 * 提交注册需要的信息
 * Created by huichuan on 16/6/2.
 */
public class LoginBean extends SessionBaseBean {

    public String action;
    public String mobile_phone;// type==TYPE_NORMAL时，必选 用户提交的手机号
    public String password;// type==TYPE_NORMAL时，必选 用户密码（已使用md5 32位加密） 用户密码强度的检查需在前端做出
    public int type = TYPE_NORMAL;// 账号类型（0:普通登陆 1:微博登陆 2:微信登陆 3:QQ登陆）
    public String xuid;// 第三方账号id (QQ提交的是openId)
    public String auth_data;// 第三方账号authdata (格式json)

    public LoginBean(String mobile_phone, String password) {
        super(APIService.SESSION_LOGIN);
        this.mobile_phone = mobile_phone;
        this.password = password;

    }

    /**
     * 设置第三方登录信息
     * @param type {@link #TYPE_NORMAL}{@link #TYPE_WEIBO}{@link #TYPE_WEIXIN}{@link #TYPE_QQ}
     * @param xuid
     * @param auth_data
     */
    public LoginBean(int type,String xuid,String auth_data) {
        super(APIService.SESSION_LOGIN);
        this.type = type;
        this.xuid = xuid;
        this.auth_data = auth_data;

    }


    public String getAuthData() {
        return auth_data==null?"":auth_data;
    }

    public String getXuid() {
        return xuid==null?"":xuid;
    }
}
