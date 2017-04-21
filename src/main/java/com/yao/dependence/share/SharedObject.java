package com.yao.dependence.share;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.yao.dependence.sso.QQSdk;
import com.yao.dependence.sso.WeiboSDK;
import com.yao.dependence.sso.WeixinSDK;
import com.yao.devsdk.SdkConfig;

import java.util.ArrayList;

public class SharedObject implements Parcelable {

    /**
     * 分享到微博，分享的是文章链接
     */
    public static final int SHARE_TYPE_WEIBO = 1;
    /**
     * 分享到微博，以图片+文字的形式
     */
    public static final int SHARE_TYPE_WEIBO_IMAGE = 2;
    /**
     * 分享到微信
     */
    public static final int SHARE_TYPE_WEIXIN = 3;
    /**
     * 分享到朋友圈
     */
    public static final int SHARE_TYPE_PENGYOUQUAN = 4;
    /**
     * 分享到QQ
     */
    public static final int SHARE_TYPE_QQ = 5;
    /**
     * 分享到QQ空间
     */
    public static final int SHARE_TYPE_QZone = 6;
    /**
     * 微信收藏
     */
    public static final int SHARE_TYPE_WEIXIN_FAVORITE = 7;

    public static final int REQUEST_CODE_SHARE = 1024;

    public static final String Extra_Share_Not_Weixin = "extra.share.not.weixin";
    public static final String Extra_Share_Object = "extra.share.object";

    private Context appContext = SdkConfig.getAppContext();

    /**
     * 默认Logo
     */
    //public static final String Default_Logo = "http://v.top.weibo.cn/statics/imgs/cates_icon/top_1.png";
    //215及之后几个版本 让红包飞 默认logo
    public static final String Default_Logo = "http://v.top.weibo.cn/statics/imgs/cates_icon/fei_512.png";

    public static final String WX_Trans_Session = "webWXSceneSession_";
    public static final String WX_Trans_Timeline = "WXSceneTimeline_";
    public static final String WX_Trans_Favorite = "WXSceneFavorite_";

    // 分享状态
    public static final int SHARE_OK = -1;//分享成功，值为Activity.RESULT_OK
    public static final int SHARE_CANCELLED = 0;//分享取消，值为Activity.RESULT_CANCELED
    public static final int SHARE_FAILED = 1;//分享失败，值为1
    public static final int SHARE_FAILED_NO_CLIENT = 2;//分享失败,未安装客户端，值为1
    public static final int SHARE_INVALID_TOKEN=3;//access_token 无效

    public int shareType = 1;
    public String url = "";//文章url
    public String title = "";//文章标题
    public String summary = "";//文章简介
    public String thumbnailUrl = "";//缩略图url
    public ArrayList<String> imageList = new ArrayList<String>();//要分享的图片列表

    /**
     * 分享工具，如果微博没有登录,可以让ShareActivity执行登录行为
     */
    public SharedObject() {

    }

    /**
     * 分享到对应平台，为了统一，都是打开WXEntryActivity再由WXEntryActivity统一处理的，幸好不分享到易信
     */
    public void share(Context context) {
        switch (shareType) {
            case SharedObject.SHARE_TYPE_PENGYOUQUAN:
            case SharedObject.SHARE_TYPE_WEIXIN:
            case SharedObject.SHARE_TYPE_WEIXIN_FAVORITE:
                WeixinSDK.shareToWeixin(this);
                break;
            case SharedObject.SHARE_TYPE_WEIBO:
            case SharedObject.SHARE_TYPE_WEIBO_IMAGE:
                WeiboSDK.shareToWeibo(context,this);
                break;
            case SharedObject.SHARE_TYPE_QQ:
            case SharedObject.SHARE_TYPE_QZone:
                QQSdk.shareToQQ(context,this);
                break;
            default:
                break;
        }
    }



    /**
     * @author pan
     *         <p>
     *         这里的代码如此简单，以至于其实可以不用Builder的
     */
    public static class Builder {

        SharedObject so = new SharedObject();

        public SharedObject build() {
            return so;
        }

        /**
         * 设置要分享的类型,SHARE_TYPE_xxx
         *
         * @param type int
         * @return this
         */
        public Builder setType(int type) {
            so.shareType = type;
            return this;
        }

        /**
         * 设定要分享的专题
         * 视频分享也用的这个
         * @param topic topic
         * @return this
         */
        public Builder setTopic(ForwardDialog.ForwardInfo topic) {
            so.title = topic.title;
            so.summary = topic.description;
            so.thumbnailUrl = topic.imageUrl;
            so.url = topic.share_url;

            return this;
        }


        /**
         * 要分享的图片列表
         */
        public Builder setImageList(ArrayList<String> imgs) {
            so.imageList = imgs;
            return this;
        }

        /**
         * 要分享的图片缩略图
         */
        public Builder setThumbnailUrl(String thumbnailUrl) {
            so.thumbnailUrl = thumbnailUrl;
            return this;
        }

        /**
         * 要分享的文章标题
         */
        public Builder setTitle(String title) {
            so.title = title;
            return this;
        }

        /**
         * 要分享的文章简介
         */
        public Builder setSummary(String summary) {
            so.summary = summary;
            return this;
        }

        /**
         * 要分享的地址
         */
        public Builder setUrl(String url) {
            so.url = url;
            return this;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shareType);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.thumbnailUrl);
        dest.writeStringList(this.imageList);
    }

    protected SharedObject(Parcel in) {
        this.shareType = in.readInt();
        this.url = in.readString();
        this.title = in.readString();
        this.summary = in.readString();
        this.thumbnailUrl = in.readString();
        this.imageList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SharedObject> CREATOR = new Parcelable.Creator<SharedObject>() {
        public SharedObject createFromParcel(Parcel source) {
            return new SharedObject(source);
        }

        public SharedObject[] newArray(int size) {
            return new SharedObject[size];
        }
    };





    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
