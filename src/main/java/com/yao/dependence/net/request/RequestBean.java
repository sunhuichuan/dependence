package com.yao.dependence.net.request;

/**
 * 请求的bean
 * Created by huichuan on 16/6/2.
 */
public abstract class RequestBean {

    private String action;

    protected RequestBean(String action){
        this.action = action;
    }




}
