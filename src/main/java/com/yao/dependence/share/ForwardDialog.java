package com.yao.dependence.share;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.yao.dependence.R;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.log.LoggerUtil;
import com.yao.devsdk.utils.DisplayUtil;
import com.yao.devsdk.utils.SdkUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by huichuan on 16/3/29.
 */
public class ForwardDialog extends QuickOptionDialog implements View.OnClickListener {

    private static final String TAG = "ForwardDialog";

    /**
     * app的context
     */
    private Context appContext = SdkConfig.getAppContext();

    private Context mContext;

    private ForwardInfo mForwardInfo;

    /**
     * 分享框第一行
     */
    static List<ForwardViewBean> forwardList1 = new ArrayList<>();
    /**
     * 分享框第二行
     */
    static List<ForwardViewBean> forwardList2 = new ArrayList<>();

    static {
//        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_WEIBO,"微博", R.drawable.detail_share_weibo_selector));
        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_WEIXIN,"微信好友", R.drawable.detail_share_wx_friend_selector));
        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_PENGYOUQUAN,"朋友圈", R.drawable.detail_share_circle_selector));
        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_QQ,"QQ", R.drawable.detail_share_qq_selector));
        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_QZONE,"QQ空间", R.drawable.detail_share_qzone_selector));
        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_FUZHI_LIANJIE,"复制链接", R.drawable.detail_share_link_selector));
        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_MORE,"更多", R.drawable.detail_share_more_selector));
//        forwardList1.add(new ForwardViewBean(ForwardViewBean.TYPE_WEIXIN_COLLECTION,"微信收藏", R.drawable.detail_share_wxcollect_selector));

//        forwardList2.add(new ForwardViewBean(ForwardViewBean.TYPE_ZHIFUBAO,"支付宝好友", R.drawable.detail_share_alipay_selector));
//        forwardList2.add(new ForwardViewBean(ForwardViewBean.TYPE_SHENGHUOQUAN,"生活圈", R.drawable.detail_share_alipay_circle_selector));
//        forwardList2.add(new ForwardViewBean(ForwardViewBean.TYPE_FUZHI_LIANJIE,"复制链接", R.drawable.detail_share_link_selector));
//        forwardList2.add(new ForwardViewBean(ForwardViewBean.TYPE_MORE,"更多", R.drawable.detail_share_more_selector));
    }


    private ForwardDialog(Context context, View view, boolean singleLine) {
        super(context, view);
        mContext = context;
        initViews(view, singleLine);
    }

    /**
     * 获取转发dialog
     *
     * @param activity
     * @return
     */
    public static ForwardDialog getDialog(Context activity, boolean singleLine) {
        if (activity instanceof Activity) {
            View forwardRoot = LayoutInflater.from(activity).inflate(R.layout.dialog_forward, null);

            ForwardDialog dialog = new ForwardDialog(activity, forwardRoot, singleLine);

            return dialog;
        } else {
            return null;
        }

    }

    private void initViews(View viewRoot, boolean singleLine) {
        Context context = getContext();

        int padding = DisplayUtil.dip2px(context, 15);

        HorizontalScrollView shareFist = (HorizontalScrollView) viewRoot.findViewById(R.id.hsv_share_first);
        LinearLayout linearLayoutFirst = new LinearLayout(context);
        linearLayoutFirst.setPadding(padding, padding, padding, padding);
        shareFist.addView(linearLayoutFirst);

        int addIndex = 0;
        for (ForwardViewBean forward : forwardList1) {
            ForwardItem item = new ForwardItem(context);
            //增加marginLeft
            if (addIndex>0){
                LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(-2, -2);
                parentParams.leftMargin = DisplayUtil.dip2px(mContext, 11);
                item.setLayoutParams(parentParams);
            }

            item.setIconInfo(forward);
            item.setOnClickListener(this);
            linearLayoutFirst.addView(item);

            addIndex++;
        }

        if (!singleLine) {
            HorizontalScrollView shareSecond = (HorizontalScrollView) viewRoot.findViewById(R.id.hsv_share_second);
            LinearLayout linearLayoutSecond = new LinearLayout(context);
            linearLayoutSecond.setPadding(padding, padding, padding, padding);
            shareSecond.addView(linearLayoutSecond);

            //重置index,供第二个集合使用
            addIndex = 0;
            for (ForwardViewBean forward : forwardList2) {
                ForwardItem item = new ForwardItem(context);
                //增加marginLeft
                if (addIndex>0){
                    LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(-2, -2);
                    parentParams.leftMargin = DisplayUtil.dip2px(mContext, 11);
                    item.setLayoutParams(parentParams);
                }

                item.setIconInfo(forward);
                item.setOnClickListener(this);
                linearLayoutSecond.addView(item);

                addIndex++;
            }
        }else {
            for (ForwardViewBean forward : forwardList2) {
                ForwardItem item = new ForwardItem(context);
                //增加marginLeft
                LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(-2, -2);
                parentParams.leftMargin = DisplayUtil.dip2px(mContext, 11);
                item.setLayoutParams(parentParams);

                item.setIconInfo(forward);
                item.setOnClickListener(this);
                linearLayoutFirst.addView(item);

            }
        }





        View shareCancel = findViewById(R.id.bottom_cancel);
        shareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    /**
     * 设置转发分享信息
     * @param info
     */
    public void setForwardInfo(ForwardInfo info){
        mForwardInfo = info;
    }



    @Override
    public void onClick(View v) {
        try {

            ForwardItem viewItem = (ForwardItem) v;
            ForwardViewBean forwardBean = viewItem.getForwardBean();
            switch (forwardBean.type) {
                case ForwardViewBean.TYPE_WEIBO: {
                    shareByWeibo(SharedObject.SHARE_TYPE_WEIBO);
                }
                break;
                case ForwardViewBean.TYPE_WEIXIN: {
                    shareByWx(SharedObject.SHARE_TYPE_WEIXIN);
                }
                break;
                case ForwardViewBean.TYPE_PENGYOUQUAN: {
                    shareByWx(SharedObject.SHARE_TYPE_PENGYOUQUAN);
                }
                break;
                case ForwardViewBean.TYPE_QQ: {
                    shareToQQ(SharedObject.SHARE_TYPE_QQ);
                }
                break;
                case ForwardViewBean.TYPE_QZONE: {
                    shareToQQ(SharedObject.SHARE_TYPE_QZone);
                }
                break;
                case ForwardViewBean.TYPE_WEIXIN_COLLECTION: {
                    shareByWx(SharedObject.SHARE_TYPE_WEIXIN_FAVORITE);
                }
                break;
                case ForwardViewBean.TYPE_FUZHI_LIANJIE: {
                    copyLink();
                }
                break;
                case ForwardViewBean.TYPE_MORE: {
                    shareMore();
                }
                break;
            }

            this.dismiss();

            SdkUtil.showDebugToast("点击的type = " + forwardBean.type);


        } catch (Exception e) {
            LoggerUtil.e(TAG, "点击事件异常", e);
        }
    }


    public void shareByWeibo(int type) {
        if (mForwardInfo == null){
            return;
        }
        if (!SdkUtil.isNetworkConnected(appContext)) {
            SdkUtil.showToast(appContext,"网络不给力");
            return;
        }
        new SharedObject.Builder().setType(type)
                .setTopic(mForwardInfo).build()
                .share(mContext);

    }



    /**
     * 分享到微信
     * @param type
     */
    public void shareByWx(int type) {
        if (mForwardInfo == null){
            return;
        }
        new SharedObject.Builder().setType(type).setTopic(mForwardInfo).build().share(mContext);

    }


    public void shareToQQ(int type) {
        if (mForwardInfo == null){
            return;
        }
        if (!SdkUtil.isNetworkConnected(appContext)) {
            SdkUtil.showToast(appContext,"网络不给力");
            return;
        }
        new SharedObject.Builder().setType(type)
                .setTopic(mForwardInfo).build().share(mContext);
    }


    /**
     * 拷贝链接
     */
    public void copyLink() {
        try {
            if (mForwardInfo!=null && !TextUtils.isEmpty(mForwardInfo.share_url)){
                ClipboardManager clipboardManager = (ClipboardManager) appContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, mForwardInfo.share_url));
                SdkUtil.showToast(appContext, "复制链接成功");
            }
        } catch (Exception ignored) {

        }

    }


    /**
     * 分享到更多
     */
    public void shareMore() {
        if (mForwardInfo!=null){
            Intent moreIntent = new Intent(Intent.ACTION_SEND);
            moreIntent.putExtra(Intent.EXTRA_TEXT,
                    appContext.getString(R.string.app_name) + "【" + mForwardInfo.title
                            + "】" + mForwardInfo.share_url);
            moreIntent.setType("text/plain");
            moreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(moreIntent);
        }
    }




    /**
     * 转发按钮的bean
     */
    static class ForwardViewBean {

        public static final int TYPE_WEIBO = 1;
        public static final int TYPE_WEIXIN = 2;
        public static final int TYPE_PENGYOUQUAN = 3;
        public static final int TYPE_QQ = 4;
        public static final int TYPE_QZONE = 5;
        public static final int TYPE_WEIXIN_COLLECTION = 6;
        public static final int TYPE_FUZHI_LIANJIE = 9;
        public static final int TYPE_MORE = 10;


        int type;
        String icon_name;
        int icon_resource_id;

        ForwardViewBean(int type, String name, int resId) {
            this.type = type;
            icon_name = name;
            icon_resource_id = resId;
        }
    }


    /**
     * 转发分享需要的信息
     */
    public static class ForwardInfo implements Serializable {
        private static final long serialVersionUID = -2574203466632019234L;
        public String title;
        public String description;
        /**
         * 正文有此字段
         */
        public String content;
        public String imageUrl;
        public String share_url;


        public ForwardInfo(String title,String description,String imageUrl,String share_url){
            this.title = title;
            this.description = description;
            this.imageUrl = imageUrl;
            this.share_url = share_url;
        }
        public ForwardInfo(String title,String description,String content,String imageUrl,String share_url){
            this.title = title;
            this.description = description;
            this.content = content;
            this.imageUrl = imageUrl;
            this.share_url = share_url;
        }

    }


}
