package com.yao.dependence.net.request;

/**
 * 请求的bean
 * Created by huichuan on 16/6/2.
 */
public abstract class SessionBaseBean extends RequestBean{

    //普通
    public static final int TYPE_NORMAL = 0;
    //微博
    public static final int TYPE_WEIBO = 1;
    //微信
    public static final int TYPE_WEIXIN = 2;
    //QQ
    public static final int TYPE_QQ = 3;


    protected SessionBaseBean(String action) {
        super(action);
    }
}
