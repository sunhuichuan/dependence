package com.yao.dependence.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.yao.dependence.net.RequestManager;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.utils.SdkUtil;

/**
 * 方便自定义控件继承的FrameLayout
 * Created by huichuan on 16/9/22.
 */
public abstract class BaseCustomFrameLayout<T> extends FrameLayout {


    protected Context appContext = SdkConfig.getAppContext();
    protected RequestManager mRequestManager;
    protected Activity thisContext;
    protected T viewInfo;


    public BaseCustomFrameLayout(Activity context) {
        this(context,null);
    }

    public BaseCustomFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseCustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()){
            return;
        }
        //在这里初始化实数无奈啊，这是为了xml预览是不报错
        mRequestManager = RequestManager.getInstance();

        if (context instanceof Activity){
            thisContext = (Activity) context;
        }else{
            throw new IllegalStateException("创建此View的Context须为Activity");
        }
        initViews(context);
    }

    /**
     * 初始化子View
     * @param context
     */
    public abstract void initViews(Context context);


    public void updateSubViews(T viewInfo){
        if (viewInfo == null){
            SdkUtil.showToast(appContext,"信息获取错误");
            setVisibility(View.GONE);
            return;
        }

        this.viewInfo = viewInfo;
        refreshSubView(viewInfo);
    }


    protected abstract void refreshSubView(T viewInfo);




}
