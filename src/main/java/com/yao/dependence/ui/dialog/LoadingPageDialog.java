package com.yao.dependence.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yao.dependence.R;
import com.yao.dependence.utils.SchemeKit;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.factory.ViewFactory;
import com.yao.devsdk.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载页面数据的dialog
 * Created by huichuan on 16/9/20.
 */
public class LoadingPageDialog extends CenterDialog{



    //TODO 有问题，不能居中，先不使用它了
    public LoadingPageDialog(Activity context) {
        super(context);
        int contentSize = DisplayUtil.dip2px(appContext,120);
        LinearLayout contentView = new LinearLayout(thisContext);

        LinearLayout innerParent = new LinearLayout(thisContext);
        innerParent.setOrientation(LinearLayout.VERTICAL);
        innerParent.setBackgroundColor(DisplayUtil.getColor(appContext,android.R.color.holo_orange_light));

        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(contentSize,contentSize);
        parentParams.gravity = Gravity.CENTER;
        contentView.addView(innerParent,parentParams);
        //加载中文字
        TextView infoView = ViewFactory.createTextView(thisContext,"加载中");
        innerParent.addView(infoView);

        setContentView(contentView);
    }



//    public LoadingPageDialog(Activity context) {
//        super(context);
//        int size = DisplayUtil.dip2px(appContext,80);
//        LinearLayout contentView = new LinearLayout(thisContext);
//        contentView.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
//        params.gravity = Gravity.CENTER;
//        //进度条
//        ProgressBar pb = new ProgressBar(thisContext);
//
//        TextView infoView = ViewFactory.createTextView(thisContext,"加载中");
//        infoView.setBackgroundColor(DisplayUtil.getColor(appContext,android.R.color.holo_orange_light));
//        contentView.addView(infoView,params);
//        setContentView(contentView);
//    }


}
