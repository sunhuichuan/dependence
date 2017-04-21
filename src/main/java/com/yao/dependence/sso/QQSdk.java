package com.yao.dependence.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yao.dependence.share.SharedObject;
import com.yao.dependence.share.activity.EmptyQQActivity;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.log.LoggerUtil;
import com.yao.devsdk.utils.SdkUtil;

import org.json.JSONObject;

/**
 * QQ 授权 sdk
 * Created by huichuan on 16/5/28.
 */
public class QQSdk {
    private static final String TAG = "QQSdk";

    //wangwang AppId
    //    final String APP_ID = "1104986549";

    private static QQSdk ourInstance = new QQSdk();

    public static QQSdk getInstance() {
        return ourInstance;
    }


    Context appContext = SdkConfig.getAppContext();
    Tencent mTencent;
    //xiegang AppId
    final String APP_ID = "1105605399";


    private QQSdk() {
        mTencent = Tencent.createInstance(APP_ID, appContext);
    }

    public Tencent getTencent() {
        return mTencent;
    }

    /**
     * //TODO 还不是很明白此方法的作用和含义
     * @param jsonObject
     */
    public void initOpenIdAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
            LoggerUtil.e(TAG, "异常", e);
        }
    }




    public void login(Activity activity, String string, IUiListener listener){
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, string, listener);
            LoggerUtil.d(TAG, "SDKQQAgentPref  FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            SdkUtil.showDebugToast("QQ session 无效");
        }
    }


    /**
     * QQ是否安装了
     * @return
     */
    public boolean isQQInstalled(){
        boolean isInstalled = SdkUtil.appIsInstalled(appContext, "com.tencent.mobileqq");
        return isInstalled;
    }


    /**
     * 分享到QQ
     */
    public static void shareToQQ(Context context,SharedObject so){
        Intent intent = new Intent(context, EmptyQQActivity.class);
        intent.putExtra(SharedObject.Extra_Share_Object,so);
        if (context instanceof Activity){
            context.startActivity(intent);
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {

            UserInfo userInfo = new UserInfo(appContext, mTencent.getQQToken());
            userInfo.getUserInfo(new IUiListener() {

                @Override
                public void onError(UiError e) {
                    String error = e.errorCode + "," + e.errorMessage + "," + e.errorDetail;
                    LoggerUtil.i(TAG, "失败：" + error);
                }

                @Override
                public void onComplete(final Object response) {
                    if (response != null && (response instanceof JSONObject)) {
                        //response是一个json
                        JSONObject userInfo = (JSONObject) response;

                        String nickName = userInfo.optString("nickname");
                        String gender = userInfo.optString("gender");
                        String avatarUrl = userInfo.optString("figureurl_qq_2");
                        String province = userInfo.optString("province");
                        String city = userInfo.optString("city");

//                        UserQQ user = new UserQQ(nickName, gender, avatarUrl, province, city);

                    }

                }

                @Override
                public void onCancel() {

                }
            });

        } else {
            SdkUtil.showDebugToast("获取QQ信息 session 无效");
        }
    }




}
