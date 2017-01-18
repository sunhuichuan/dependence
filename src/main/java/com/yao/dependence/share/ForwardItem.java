package com.yao.dependence.share;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yao.dependence.R;
import com.yao.dependence.sso.AlipaySDK;
import com.yao.dependence.sso.WeixinSDK;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.utils.DisplayUtil;
import com.yao.devsdk.utils.SdkUtil;
import com.yao.devsdk.utils.ViewUtils;


/**
 *
 * Created by huichuan on 16/3/29.
 */
public class ForwardItem extends RelativeLayout {


    private Context appContext = SdkConfig.getAppContext();
    private Context mContext;

    private ImageView iv_icon;
    private TextView tv_name;

    ForwardDialog.ForwardViewBean mForwardBean;

    public ForwardItem(Context context) {
        super(context);

        initView(context);
    }

    private void initView(Context context) {
        mContext = context;

        iv_icon = new ImageView(context);
        iv_icon.setId(ViewUtils.generateViewId());
        iv_icon.setImageResource(R.drawable.detail_share_weibo_selector);

        tv_name = new TextView(context);
        tv_name.setGravity(Gravity.CENTER);
        int textColor = context.getResources().getColor(R.color.text_color_333333);
        tv_name.setTextColor(textColor);
        tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        RelativeLayout.LayoutParams tv_params = new RelativeLayout.LayoutParams(-2, -2);
        tv_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_params.addRule(RelativeLayout.BELOW, iv_icon.getId());
        tv_params.topMargin = DisplayUtil.dip2px(context, 10);
        tv_name.setLayoutParams(tv_params);

        addView(iv_icon);
        addView(tv_name);



    }


    public void setIconResourceId(int resourceId) {
        iv_icon.setImageResource(resourceId);
    }

    public void setIconName(String name) {
        tv_name.setText(name);
    }


    public void setIconInfo(ForwardDialog.ForwardViewBean info) {
        mForwardBean = info;
        if (info != null) {
            setIconResourceId(info.icon_resource_id);
            setIconName(info.icon_name);
            setIconStyle();
        }
    }


    public void setIconStyle() {
        if (mForwardBean != null) {
            switch (mForwardBean.type) {
                case ForwardDialog.ForwardViewBean.TYPE_WEIXIN:
                case ForwardDialog.ForwardViewBean.TYPE_PENGYOUQUAN: {

                    if (!WeixinSDK.getInstance().getWxapi().isWXAppInstalled()) {
                        //未安装
                        iv_icon.setEnabled(false);
                    }

                }
                break;
                case ForwardDialog.ForwardViewBean.TYPE_QQ:
                case ForwardDialog.ForwardViewBean.TYPE_QZONE: {
                    if (!SdkUtil.appIsInstalled(appContext, "com.tencent.mobileqq")) {
                        //未安装
                        iv_icon.setEnabled(false);
                    }
                }
                break;
                case ForwardDialog.ForwardViewBean.TYPE_ZHIFUBAO:{
                    if (AlipaySDK.getInstance().getAPApi().isZFBAppInstalled()
                            && AlipaySDK.getInstance().getAPApi().isZFBSupportAPI()){

                    }else{
                        //未安装
                        iv_icon.setEnabled(false);
                    }
                }
                break;
                case ForwardDialog.ForwardViewBean.TYPE_SHENGHUOQUAN: {
                    if (AlipaySDK.getInstance().getAPApi().isZFBAppInstalled()
                            && AlipaySDK.getInstance().getAPApi().isZFBSupportAPI()
                            && AlipaySDK.getInstance().getAPApi().getZFBVersionCode()>=84){

                    }else{
                        //未安装
                        iv_icon.setEnabled(false);
                    }
                }
                break;
            }


        }
    }


    public ForwardDialog.ForwardViewBean getForwardBean() {
        return mForwardBean;
    }





}
