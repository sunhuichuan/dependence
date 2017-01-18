package com.yao.dependence.share.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.yao.dependence.share.SharedObject;
import com.yao.dependence.sso.AccessTokenKeeper;
import com.yao.dependence.sso.WeiboSDK;
import com.yao.dependence.ui.BaseActivity;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.utils.SdkUtil;

/**
 * 用于微博登录的空Activity
 */
public class EmptyWeiboActivity extends BaseActivity implements IWeiboHandler.Response {

    private Context appContext = SdkConfig.getAppContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv_content = new TextView(this);
        tv_content.setText("微博分享页面");
        tv_content.setTextColor(Color.RED);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        setContentView(tv_content, layoutParams);

        Intent intent = getIntent();

        SharedObject so = intent.getParcelableExtra(SharedObject.Extra_Share_Object);
        if (so == null || (so.shareType != SharedObject.SHARE_TYPE_WEIBO && so.shareType != SharedObject.SHARE_TYPE_WEIBO_IMAGE)) {

            SdkUtil.showToast(appContext, "分享微博失败");
            finishPage();
        } else {
            shareWeibo(this, so);
        }


    }


    Oauth2AccessToken mAccessToken;
    SsoHandler mSsoHandler;
    IWeiboShareAPI mWeiboShareAPI;

    /**
     * 分享给QQ好友
     */
    private void shareWeibo(Activity activity, SharedObject so) {


        //注册授权
        AuthInfo mAuthInfo = new AuthInfo(this, WeiboSDK.APP_KEY, WeiboSDK.REDIRECT_URL, WeiboSDK.SCOPE);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());

//        if (HeadlineApplication.isLogin) {
//            final Intent intent = new Intent();
//            intent.setClass(this, ShareActivity.class);
//            intent.putExtra(ShareActivity.MID, so.mid);
//            intent.putExtra(ShareActivity.TITLE, so.title);
//            intent.putExtra(ShareActivity.SUMMARY, so.summary);
//            intent.putExtra(ShareActivity.SHORT_URL, so.url);
//            intent.putExtra(ShareActivity.BITMAP_URL, so.thumbnailUrl);
//            intent.putExtra(ShareActivity.OID, so.oid);
//            intent.putExtra(ShareActivity.UICODE, so.uicode);
//            intent.putExtra(ShareActivity.EXTRA, so.extra);
//            intent.putExtra(ShareActivity.FID, so.fid);
//            intent.putExtra(ShareActivity.TYPE, so.shareType);
//            intent.putExtra(ShareActivity.AUTHOR, so.author);
//
//            Handler handler = new Handler();
//            if (TextUtils.isEmpty(SharedPreferencesUtil.getSetting().user_name.getVal())) {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivityForResult(intent, REQUEST_CODE_SHARE);
//                        overridePendingTransition(R.anim.anim_start_in,
//                                R.anim.anim_start_out);
//                    }
//                }, 500);
//            } else {
//                startActivityForResult(intent, REQUEST_CODE_SHARE);
//                overridePendingTransition(R.anim.anim_start_in,
//                        R.anim.anim_start_out);
//            }
//        } else {
//            ActivityLoginDelegate.startLoginActivity(this, REQUEST_CODE_LOGIN_FOR_SHARE);
//        }

        //在 onCreate 凼数创建微博分享接口实例,并进行注册,请确保先注册,后分享
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, WeiboSDK.APP_KEY);
        mWeiboShareAPI.registerApp();// 将应用注册到微博客户端




        //执行分享
        sendMultiMessage(true);
    }

    //创建要分享的内容
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "测试分享的Text";
        return textObject;
    }


    //通过 IWeiboShareAPI#sendRequest 唤起微博宠户端发博器迚行分享
    private void sendMultiMessage(boolean hasText) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(this, request); //发送请求消息到微博,唤起微博分享界面
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this); //当前应用唤起微博分享后,返回当前应用
    }

    //实现 IWeiboHandler#Response 接口,接收分享后微博返回的数据
    @Override
    public void onResponse(BaseResponse baseResp) {//接收微客户端博请求的数据。
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                SdkUtil.showToast(appContext,"分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                SdkUtil.showToast(appContext,"取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                SdkUtil.showToast(appContext,"分享失败");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(appContext, mAccessToken); //保存Token
                //.........
            } else {


            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }

        @Override
        public void onCancel() {

        }
    }
}