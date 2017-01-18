package com.yao.dependence.net.request;

import com.yao.dependence.net.APIService;


/**
 * 提交注册需要的信息
 * Created by huichuan on 16/6/2.
 */
public class RegisterBean extends SessionBaseBean {

    public String action;// = signup_submit
    public String mobile_phone;// 必选 用户提交的手机号
    public String nickname;// 必选 用户昵称（2-9个中文英文数字和下划线字符）
    public String password;// 必选 用户密码（已使用md5 32位加密） 用户密码强度的检查需在前端做出
    public String code;// 必选 手机验证码 （验证码有可能以0开头，所以验证码是字符串而不是int）
    public int type = TYPE_NORMAL;// 账号类型（0:普通登陆 1:微博登陆 2:微信登陆 3:QQ登陆）
    public String xuid;// 第三方账号id (QQ提交的是openId)
    public String auth_data;// 第三方账号authdata (格式json)

    public RegisterBean(String mobile_phone, String code, String password, String nickname) {
        super(APIService.SIGNUP_SUBMIT);
        this.mobile_phone = mobile_phone;
        this.code = code;
        this.password = password;
        this.nickname = nickname;

    }


    /**
     * 设置第三方登录信息
     * @param type {@link #TYPE_NORMAL}{@link #TYPE_WEIBO}{@link #TYPE_WEIXIN}{@link #TYPE_QQ}
     * @param xuid
     * @param auth_data
     */
    public void setXuidAndData(int type,String xuid,String auth_data) {
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
