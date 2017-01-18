package com.yao.dependence.sso;

import android.content.Context;
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
public class WeixinSDK {
    private static WeixinSDK ourInstance = new WeixinSDK();

    public static WeixinSDK getInstance() {
        return ourInstance;
    }


    public static final String APP_ID  = "wx097496417c96d56b";
    //商户id
    public static final String MERCHANT_ID  = "1348243101";
    public static final String APP_SECRET  = "b082889528572795c012d7b560d16386";
//    public static final String AUTHORIZATION_CODE = "6731ba1ff38edb2731251c127c34401c";



    private Context appContext = SdkConfig.getAppContext();
    IWXAPI wxapi;

    private WeixinSDK() {
        //通过WxApiFactory工厂，获取IWXApi的实例
        wxapi = WXAPIFactory.createWXAPI(appContext, APP_ID, true);
        //将应用的appId注册到微信
        wxapi.registerApp(APP_ID);

    }


    /**获取微信 Api 的实例*/
    public IWXAPI getWxapi() {
        return wxapi;
    }

    /**请求oauth 认证*/
    public void requestOAuth(){
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "xiegang_"+ SdkConfig.deviceId;
        wxapi.sendReq(req);
    }


    /**
     * 分享到微信
     */
    public static void shareToWeixin(SharedObject so){
        IWXAPI mWXApi = WeixinSDK.getInstance().getWxapi();
        if (mWXApi.isWXAppInstalled()) {
            byte[] mForwardByte = getThumb(so.thumbnailUrl, SdkConfig.getAppContext());
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = so.url;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = so.title;
            msg.description = so.summary;
            msg.thumbData = mForwardByte;

            SendMessageToWX.Req req = new SendMessageToWX.Req();

            req.message = msg;
            if (so.shareType == SharedObject.SHARE_TYPE_WEIXIN) {
                req.transaction = SharedObject.WX_Trans_Session + String.valueOf(System.currentTimeMillis());
                req.scene = SendMessageToWX.Req.WXSceneSession;
            } else if (so.shareType == SharedObject.SHARE_TYPE_PENGYOUQUAN) {
                req.transaction = SharedObject.WX_Trans_Timeline + String.valueOf(System.currentTimeMillis());
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else {
                //微信收藏
                req.transaction = SharedObject.WX_Trans_Favorite + String.valueOf(System.currentTimeMillis());
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
            }
            mWXApi.sendReq(req);
        }
    }



    public static byte[] getThumb(String url, Context context) {
        //最小边的长度不得超过此值
        final int dimenThresh = 256;
        Bitmap bitmap = null;
        byte[] imageData = new byte[0];
        try {
            File file = ImageLoader.getInstance().getDiskCache().get(url);
            if (file != null && file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());//小米上在这里inJustDecodeBounds不能使用

                if (bitmap != null) {
                    int scale = 1;
                    while (bitmap.getWidth() / scale > dimenThresh && bitmap.getHeight() / scale >
                            dimenThresh) {
                        scale *= 2;
                    }
                    options.inSampleSize = scale;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    int dim = Math.min(bitmap.getWidth(), bitmap.getHeight());
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, dim, dim);
                    float step = 1.2f;
                    imageData = bmpToByteArray(bitmap, false);
                    while (imageData.length > 32000) {
                        Matrix matrix = new Matrix();
                        matrix.setScale(1 / step, 1 / step);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                matrix, false);
                        imageData = bmpToByteArray(bitmap, false);
                    }
                }
            }
        } catch (Exception ignored) {

        }

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            imageData = bmpToByteArray(bitmap, false);
        }

        return imageData;
    }







    public static byte[] bmpToByteArray(final Bitmap bmp,
                                        final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception ignored) {

        }
        return result;
    }





}
