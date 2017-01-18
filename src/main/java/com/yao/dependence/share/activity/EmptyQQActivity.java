package com.yao.dependence.share.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yao.dependence.R;
import com.yao.dependence.share.SharedObject;
import com.yao.dependence.sso.QQSdk;
import com.yao.dependence.ui.BaseActivity;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.utils.SdkUtil;

import java.util.ArrayList;

/**
 * 用于QQ登录的空Activity
 */
public class EmptyQQActivity extends BaseActivity {

    private Context appContext = SdkConfig.getAppContext();
    private Tencent tencent = QQSdk.getInstance().getTencent();
    private QQListener qqListener = new QQListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv_content = new TextView(this);
        tv_content.setText("QQ分享页面");
        tv_content.setTextColor(Color.RED);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2,-2);
        setContentView(tv_content, layoutParams);

        Intent intent = getIntent();

        SharedObject so = intent.getParcelableExtra(SharedObject.Extra_Share_Object);
        if (so == null || (so.shareType != SharedObject.SHARE_TYPE_QQ && so.shareType != SharedObject.SHARE_TYPE_QZone)) {

            SdkUtil.showToast(appContext, "分享QQ失败");
            finishPage();
        } else {
            shareQQ(this, so);
        }


    }


    /**
     * 分享给QQ好友
     */
    private void shareQQ(Activity activity, SharedObject so) {

        if (QQSdk.getInstance().isQQInstalled()) {



            Bundle bundle = new Bundle();

            if (TextUtils.isEmpty(so.thumbnailUrl)) {
//                so.thumbnailUrl = so.Default_Logo;
            }

            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, so.summary);// 选填
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, so.url);// 必填

            if (so.shareType == SharedObject.SHARE_TYPE_QQ) {
                //QQ
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, so.thumbnailUrl);
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, so.title);// 必填
                tencent.shareToQQ(activity, bundle, qqListener);
            } else {
                //空间
                if (so.imageList == null || so.imageList.size() == 0) {
                    so.imageList = new ArrayList<>();
                    so.imageList.add(so.thumbnailUrl);
                }
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, so.imageList);

                bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                        QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                String appName = SdkConfig.getAppContext().getString(R.string.app_name);
                bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "《" + appName + "》" + so.title);// 必填
                tencent.shareToQzone(activity, bundle, qqListener);
            }


        } else {
            SdkUtil.showToast(SdkConfig.getAppContext(), "未找到QQ应用");
            finishPage();
        }
    }


    /*
        * 分享到QQ和空间的回调接口
        */
    public class QQListener implements IUiListener {

        @Override
        public void onCancel() {
            SdkUtil.showToast(appContext, "取消分享");
            finishPage();
        }

        @Override
        public void onComplete(Object arg0) {
            SdkUtil.showToast(appContext, "分享成功");
            finishPage();
        }

        @Override
        public void onError(UiError error) {
            SdkUtil.showToast(appContext, "分享失败");
            finishPage();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        tencent.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode,resultCode,data,qqListener);
    }

}
