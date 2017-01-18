package com.yao.dependence.event;


/**
 *
 * Created by huichuan on 16/5/28.
 */
public class WeiXinLoginEvent {


    public int errorCode;
    public String code;
    public String openId;
    public String state;
    public String lang;
    public String country;


    public WeiXinLoginEvent(int errorCode,String code,String openId,String state,String lang,String country) {
        this.errorCode = errorCode;
        this.code = code;
        this.openId = openId;
        this.lang = lang;
        this.state = state;
        this.country = country;
    }


    @Override
    public String toString() {
        return "WeiXinLoginEvent{" +
                "errorCode=" + errorCode +
                ", code='" + code + '\'' +
                ", openId='" + openId + '\'' +
                ", state='" + state + '\'' +
                ", lang='" + lang + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
