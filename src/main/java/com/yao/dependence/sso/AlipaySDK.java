package com.yao.dependence.sso;

import android.content.Context;
import android.text.TextUtils;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.yao.dependence.share.SharedObject;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.utils.SdkUtil;

/**
 * 支付宝 sdk 的授权
 *
 * 需要创建[包名].apshare.ShareEntryActivity,用以接收回调通知
 *
 *
 * Created by huichuan on 16/5/28.
 */
public class AlipaySDK {
    private static AlipaySDK ourInstance = new AlipaySDK();

    public static AlipaySDK getInstance() {
        return ourInstance;
    }


    public static final String APP_ID  = "wx097496417c96d56b";
    //商户id
    public static final String MERCHANT_ID  = "1348243101";
    public static final String APP_SECRET  = "b082889528572795c012d7b560d16386";
//    public static final String AUTHORIZATION_CODE = "6731ba1ff38edb2731251c127c34401c";



    private Context appContext = SdkConfig.getAppContext();
    IAPApi zfbApi;

    private AlipaySDK() {
        //APAPIFactory，IAPApi
        zfbApi = APAPIFactory.createZFBApi(appContext, APP_ID, false);


    }


    /**获取支付宝 Api 的实例*/
    public IAPApi getAPApi() {
        return zfbApi;
    }

//    /**请求oauth 认证*/
//    public void requestOAuth(){
//        // send oauth request
//        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "xiegang_"+ SdkConfig.deviceId;
//        wxapi.sendReq(req);
//    }


    /**
     * 分享到支付宝
     * @param so
     */
    public static void shareToAlipay(SharedObject so){
        IAPApi api = AlipaySDK.getInstance().getAPApi();
        if (api.isZFBAppInstalled()) {
            if (api.isZFBSupportAPI()) {
                APWebPageObject webPageObject = new APWebPageObject();
                webPageObject.webpageUrl = so.url;
                APMediaMessage webMessage = new APMediaMessage();
                webMessage.mediaObject = webPageObject;
                webMessage.title = so.title;
                webMessage.description = so.summary;
                if (TextUtils.isEmpty(so.thumbnailUrl)) {
//                    so.thumbnailUrl = so.Default_Logo;
                }
                webMessage.thumbUrl = so.thumbnailUrl;
                SendMessageToZFB.Req webReq = new SendMessageToZFB.Req();
                webReq.message = webMessage;
                webReq.transaction = String.valueOf(System.currentTimeMillis());
                if(so.shareType==SharedObject.SHARE_TYPE_ALIPAY_FRIENDS){
                    webReq.scene=SendMessageToZFB.Req.ZFBSceneSession;

                }else if(so.shareType==SharedObject.SHARE_TYPE_ALIPAY_CIRCLE){
                    webReq.scene=SendMessageToZFB.Req.ZFBSceneTimeLine;
                }
                api.sendReq(webReq);
            } else {
                SdkUtil.showToast(SdkConfig.getAppContext(), "支付宝当前版本不支持");
            }
        } else {
            SdkUtil.showToast(SdkConfig.getAppContext(), "支付宝还没有安装");
        }
    }




}
