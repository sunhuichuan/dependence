package com.yao.dependence.share.event;

import com.yao.dependence.share.SharedObject;

/**
 * 分享event
 * Created by huichuan on 16/7/10.
 */
public class ShareEvent {


    public static final int ERROR_SUCCESS = 1;
    public static final int ERROR_FAILED = 2;
    public static final int ERROR_CANCEL = 3;


    int shareType;
    int errorCode;

    public ShareEvent(int shareType, int errorCode){
        this.shareType = shareType;
        this.errorCode = errorCode;
    }


    public boolean isWeixin(){
        return shareType == SharedObject.SHARE_TYPE_WEIXIN;
    }


    public boolean isAlipay(){
        return shareType == SharedObject.SHARE_TYPE_ALIPAY_FRIENDS;
    }


    public boolean isShareSuccess(){
        return errorCode == ERROR_SUCCESS;
    }

    public boolean isShareFailed(){
        return errorCode == ERROR_FAILED;
    }

    public boolean isShareCancel(){
        return errorCode == ERROR_CANCEL;
    }



}
