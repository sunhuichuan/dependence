package com.yao.dependence.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yao.dependence.R;
import com.yao.dependence.share.SharedObject;
import com.yao.dependence.share.activity.EmptyQQActivity;
import com.yao.dependence.share.activity.EmptyWeiboActivity;
import com.yao.devsdk.SdkConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 微信 sdk 的授权
 *
 * 需要创建[包名].wxapi.WXEntryActivity,用以接收回调通知
 *
 *
 * Created by huichuan on 16/5/28.
 */
public class WeiboSDK {
    private static WeiboSDK ourInstance = new WeiboSDK();

    public static WeiboSDK getInstance() {
        return ourInstance;
    }


    public static final String SCOPE_MESSAGE = "email,direct_messages_read,direct_messages_write,";
    public static final String SCOPE_FRIEND = "friendships_groups_read,friendships_groups_write,statuses_to_me_read,";
    public static final String SCOPE_FOLLOW_APP = "follow_app_official_microblog,";
    public static final String SCOPE_INVITATION = "invitation_write";


    // 应用的APP_KEY
    public static final String APP_KEY  = "402137123";
    // 应用的回调页
    public static final String REDIRECT_URL = "http://www.sina.com";
    // 应用申请的高级权限
    public static final String SCOPE = SCOPE_MESSAGE + SCOPE_FRIEND + SCOPE_FOLLOW_APP + SCOPE_INVITATION;



    private Context appContext = SdkConfig.getAppContext();

    private WeiboSDK() {

    }


//    /**获取微信 Api 的实例*/
//    public IWXAPI getWxapi() {
//        return wxapi;
//    }
//
//    /**请求oauth 认证*/
//    public void requestOAuth(){
//        // send oauth request
//        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "xiegang_"+ SdkConfig.deviceId;
//        wxapi.sendReq(req);
//    }
//
//
    /**
     * 分享到微博
     */
    public static void shareToWeibo(Context context,SharedObject so){
        Intent intent = new Intent(context, EmptyWeiboActivity.class);
        intent.putExtra(SharedObject.Extra_Share_Object,so);
        if (context instanceof Activity){
            context.startActivity(intent);
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
//
//
//
//    public static byte[] getThumb(String url, Context context) {
//        //最小边的长度不得超过此值
//        final int dimenThresh = 256;
//        Bitmap bitmap = null;
//        byte[] imageData = new byte[0];
//        try {
//            File file = ImageLoader.getInstance().getDiskCache().get(url);
//            if (file != null && file.exists()) {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());//小米上在这里inJustDecodeBounds不能使用
//
//                if (bitmap != null) {
//                    int scale = 1;
//                    while (bitmap.getWidth() / scale > dimenThresh && bitmap.getHeight() / scale >
//                            dimenThresh) {
//                        scale *= 2;
//                    }
//                    options.inSampleSize = scale;
//                    options.inPreferredConfig = Bitmap.Config.RGB_565;
//                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
//                    int dim = Math.min(bitmap.getWidth(), bitmap.getHeight());
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, dim, dim);
//                    float step = 1.2f;
//                    imageData = bmpToByteArray(bitmap, false);
//                    while (imageData.length > 32000) {
//                        Matrix matrix = new Matrix();
//                        matrix.setScale(1 / step, 1 / step);
//                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
//                                matrix, false);
//                        imageData = bmpToByteArray(bitmap, false);
//                    }
//                }
//            }
//        } catch (Exception ignored) {
//
//        }
//
//        if (bitmap == null) {
//            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//            imageData = bmpToByteArray(bitmap, false);
//        }
//
//        return imageData;
//    }
//
//
//
//
//
//
//
//    public static byte[] bmpToByteArray(final Bitmap bmp,
//                                        final boolean needRecycle) {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
//        if (needRecycle) {
//            bmp.recycle();
//        }
//        byte[] result = output.toByteArray();
//        try {
//            output.close();
//        } catch (Exception ignored) {
//
//        }
//        return result;
//    }





}
