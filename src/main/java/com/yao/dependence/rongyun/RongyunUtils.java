package com.yao.dependence.rongyun;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;

import com.yao.dependence.net.RequestManager;
import com.yao.dependence.model.RongyunToken;
import com.yao.dependence.model.User;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.constants.SdkConst;
import com.yao.devsdk.log.LogUtil;
import com.yao.devsdk.net.response.HttpResult;
import com.yao.devsdk.utils.ProcessUtils;
import com.yao.devsdk.utils.SdkUtil;

import java.io.IOException;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.Response;
import rx.Subscriber;

import static io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;

/**
 * 融云工具类
 * Created by huichuan on 16/9/3.
 */
public class RongyunUtils {
    private static final String TAG = "RongyunUtils";
//    private static RongyunUtils ourInstance = new RongyunUtils();
//
//    public static RongyunUtils getInstance() {
//        return ourInstance;
//    }
//
//    private RongyunUtils() {
//    }




    /**
     * 初始化融云
     *
     * 注意：
     *
     * IMKit SDK调用第一步 初始化
     *
     * context上下文
     *
     * 只有两个进程需要初始化，主进程和 push 进程
     */
    public static void initRongyun(Context context) {

        String rongyunPushProcessName = "io.rong.push";
        String processName = ProcessUtils.getCurProcessName(context);
        String applicationPackageName = context.getApplicationInfo().packageName;
        if (applicationPackageName.equals(processName) ||
                rongyunPushProcessName.equals(processName)) {

            RongIM.init(context);

            //扩展功能自定义
            InputProvider.ExtendProvider[] provider = {
                    new ImageInputProvider(RongContext.getInstance()),//图片
                    new CameraInputProvider(RongContext.getInstance()),//相机
            };
            RongIM.resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
            //设置获取用户信息的Provider
            setUserInfoProvider();

        }


    }



    public static void loginRongyun() {
        final Context appContext = SdkConfig.getAppContext();

        SdkUtil.showDebugToast(appContext,"开始登录融云");

        if (!SdkUtil.isNetworkConnected(appContext) || !SdkConfig.isLogin()){
            LogUtil.e(TAG,"无网络 || 未登录，无法登录融云");
            return;
        }

        ConnectionStatus currentConnectionStatus =
                RongIM.getInstance().getCurrentConnectionStatus();
        if (currentConnectionStatus == ConnectionStatus.CONNECTED
                ||currentConnectionStatus == ConnectionStatus.CONNECTING){
            //已经链接或者正在连接
            SdkUtil.showDebugToast(appContext,"融云connnect状态：已经链接或者正在连接");
            return;
        }


        String portraitUri = "http://atth.eduu.com/album/201203/12/1475134_1331559643qMzc.jpg";
        RequestManager.getInstance().rongyunToken(SdkConfig.uid, "深深深", portraitUri)
                .subscribe(new Subscriber<RongyunToken>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "rongyunToken  onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "rongyunToken异常", e);
                    }

                    @Override
                    public void onNext(RongyunToken rongyun) {

                        LogUtil.i(TAG, "rongyunToken  onNext --> " + rongyun.toString());
                        httpGetTokenSuccess(appContext,rongyun.token);
                    }
                });

    }

    /**
     * 融云 第二步：connect 操作
     *
     * @param token
     */
    private static void httpGetTokenSuccess(final Context context,String token) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtil.e(TAG, "----connect onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String userId) {

                LogUtil.i(TAG, "----connect onSuccess userId----:" + userId);
                SdkUtil.showDebugToast(context,"登录成功");

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                SdkUtil.showDebugToast(context,"登录融云失败");
                LogUtil.e(TAG, "----connect onError ErrorCode----:" + e);
            }
        });
    }




    //开启私人会话
    public static void startPrivateChat(Context context,String userId, String title){
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().startPrivateChat(context, userId, title);
        }
    }

    /**
     * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
     *
     * userInfoProvider 用户信息提供者。
     * isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
     *                         如果 App 提供的 UserInfoProvider
     *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
     *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
     * UserInfoProvider
     */
    private static void setUserInfoProvider(){

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {
                if (SdkConst.DEBUG){
                    LogUtil.e(TAG,"UserInfoProvider-->获取UserId : "+userId+" 信息，当前线程："+Thread.currentThread()+"###"+Thread.currentThread().getId());
                    LogUtil.e(TAG,"UserInfoProvider-->线程id是否与主线程相等："+ (Looper.getMainLooper().getThread() == Thread.currentThread()));
                }
                try {
                    //findUserById(userId)
                    //根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                    Call<HttpResult<User>> httpResultCall = RequestManager.getInstance().userInfo(userId);
                    Response<HttpResult<User>> resultResponse = httpResultCall.execute();
                    HttpResult<User> httpResult = resultResponse.body();

                    if (httpResult.getStatus() != 1) {
//                        throw new ApiException(httpResult.getStatus(), httpResult.getMessage());
                        LogUtil.e(TAG,"获取用户信息失败："+httpResult.getStatus()+"---"+httpResult.getMessage());
                        return null;
                    }else {

                        User user = httpResult.getData();
                        String uid = user.getId();
                        String name = user.getName();
                        String avatar = user.getProfile_image_url();
                        Uri avatarUri = Uri.parse(avatar);

                        UserInfo rongyunUser = new UserInfo(uid,name,avatarUri);
                        return rongyunUser;
                    }


                } catch (IOException e) {
                    LogUtil.e(TAG,"获取用户信息异常",e);
                    return null;
                }
            }

        }, true);

    }


}
