package com.yao.dependence.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yao.dependence.R;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.constants.SdkConst;
import com.yao.devsdk.log.LogUtil;
import com.yao.devsdk.ui.SDKBaseActivity;

/**
 * 通用标题栏View
 * Created by huichuan on 16/4/20.
 */
public class TitleBarLayout extends Toolbar{
    private static final String TAG = "TitleBarLayout";

    private Context appContext = SdkConfig.getAppContext();
    private Activity thisContext;
    //根View
    private View mTitleRootView;
    TextView tv_title_name;
    ViewGroup rl_back_group;
    ImageView iv_title_back_img;
    TextView tv_title_back_text;
    ViewGroup rl_right_group;
    ImageView iv_title_right_img;
    TextView tv_title_right_text;
    //外部定义的返回按钮点击事件
    View.OnClickListener mLeftButtonClickListener;

    public TitleBarLayout(Context context) {
        this(context, null);
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }


    /**
     * 初始化view
     * @param context
     */
    public void initViews(Context context, AttributeSet attrs) {
        if (!isInEditMode()){
            //为了XML中能够预览，需要这么写一下
            thisContext = (Activity) context;
        }
        View titleView = View.inflate(context,R.layout.title_bar_layout,this);
        mTitleRootView = titleView;

        try{

            //子view
            tv_title_name = (TextView) titleView.findViewById(R.id.tv_title_name);
            rl_back_group = (ViewGroup) titleView.findViewById(R.id.rl_back_group);
            iv_title_back_img = (ImageView) titleView.findViewById(R.id.iv_title_back_img);
            tv_title_back_text = (TextView) titleView.findViewById(R.id.tv_title_back_text);
            rl_right_group = (ViewGroup) titleView.findViewById(R.id.rl_right_group);
            iv_title_right_img = (ImageView) titleView.findViewById(R.id.iv_title_right_img);
            tv_title_right_text = (TextView) titleView.findViewById(R.id.tv_title_right_text);

            //titleBar样式
            Style styleType = Style.RED;
            CharSequence titleName = "标题";
            // Styleables from XML
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.TitleBarLayout);
            if (a.hasValue(R.styleable.TitleBarLayout_styleType)) {
                styleType = Style.mapIntToValue(a.getInteger(
                        R.styleable.TitleBarLayout_styleType, 0));
            }
            if (a.hasValue(R.styleable.TitleBarLayout_titleName)) {
                titleName = a.getText(R.styleable.TitleBarLayout_titleName);
            }

            a.recycle();

            setTitleName(titleName);
            setTitleStyle(styleType);
            initItemButton();
        }catch (Exception ex){
            LogUtil.e(TAG,"titleBarLayout-->异常2",ex);
        }

        if (isInEditMode()){
            return;
        }


    }

    /**
     * 初始化默认的button样式
     */
    private void initItemButton() {
        LogUtil.i(TAG,"TitleBarLayout ---> initItemButton");
        hideRightItemView();
//        setLeftButtonContent(R.drawable.title_bar_back_white_selector);

        rl_back_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了返回按钮
                if (mLeftButtonClickListener!=null){
                    mLeftButtonClickListener.onClick(v);
                }else{
                    //没有设置，用默认的
                    finishActivity();
                }
            }
        });
    }

    private void finishActivity(){
        if (thisContext instanceof SDKBaseActivity){
            ((SDKBaseActivity)thisContext).finishPage();
        }else{
            //其他类型的Activity
            if (SdkConst.DEBUG){
                LogUtil.e(TAG,"关闭Activity-->"+thisContext.getClass().getName());
            }
            thisContext.finish();
        }

    }


    /**
     * 设置标题栏样式
     * @param style {@link Style}
     */
    public void setTitleStyle(Style style){

        Resources resource = appContext.getResources();

        int titleBgColor;
        int titleNameColor;
        ColorStateList titleSubNameColor;
        if (style == Style.RED){
            //红样式
            titleBgColor = resource.getColor(R.color.appBaseRedColor);
            titleNameColor = resource.getColor(R.color.WHITE);
            titleSubNameColor = resource.getColorStateList((R.color.title_bar_text_white_color));
            //返回箭头的样式
            setLeftButtonContent(R.drawable.title_bar_back_white_selector);
        }else{
            //白样式是默认样式,可以不设置
            titleBgColor = resource.getColor(R.color.gray_color_f7f7f7);
            titleNameColor = resource.getColor(R.color.gray_color_545454);
            titleSubNameColor = resource.getColorStateList((R.color.title_bar_text_red_color));
            //返回箭头的样式
            setLeftButtonContent(R.drawable.title_bar_back_red_selector);
        }
        mTitleRootView.setBackgroundColor(titleBgColor);
        tv_title_name.setTextColor(titleNameColor);
        tv_title_back_text.setTextColor(titleSubNameColor);
        tv_title_right_text.setTextColor(titleSubNameColor);
    }


    /**
     * 设置标题名字
     * @param titleName
     */
    public void setTitleName(CharSequence titleName){
        if (mTitleRootView != null){
            tv_title_name.setText(titleName);
        }
    }


    /**
     * 隐藏返回按钮
     * @return
     */
    public void hideBackItemView(){
        LogUtil.i(TAG,"TitleBarLayout ---> hideBackItemView");
        if (mTitleRootView !=null){
            rl_back_group.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 隐藏右上角按钮
     * @return
     */
    public void hideRightItemView(){
        LogUtil.i(TAG,"TitleBarLayout ---> hideRightItemView");
        if (mTitleRootView !=null){
            rl_right_group.setVisibility(View.INVISIBLE);
        }
    }

    public void setLeftButtonContent(int resourceId){
        setLeftButtonContent(resourceId,null);
    }
    /**
     * 设置返回按钮样式
     */
    public void setLeftButtonContent(int resourceId,OnClickListener onClickListener){
        if (mTitleRootView != null){
            rl_back_group.setVisibility(View.VISIBLE);
            iv_title_back_img.setImageResource(resourceId);
            iv_title_back_img.setVisibility(View.VISIBLE);
            tv_title_back_text.setVisibility(View.GONE);

            mLeftButtonClickListener = onClickListener;
        }
    }

    public void setLeftButtonContent(CharSequence backText){
        setLeftButtonContent(backText,null);
    }
    /**
     * 设置返回文字
     */
    public void setLeftButtonContent(CharSequence backText,OnClickListener onClickListener){
        if (mTitleRootView != null){
            rl_back_group.setVisibility(View.VISIBLE);
            tv_title_back_text.setText(backText);
            tv_title_back_text.setVisibility(View.VISIBLE);
            iv_title_back_img.setVisibility(View.GONE);

            mLeftButtonClickListener = onClickListener;
        }
    }

    /**
     * 替换左边按钮，供外部自定义
     * @param leftView
     */
    public void replaceLeftItemView(View leftView){
        rl_back_group.setVisibility(View.VISIBLE);
        rl_back_group.removeAllViews();
        rl_back_group.addView(leftView);
    }


    /**
     * 设置右上角按钮样式
     */
    public void setRightButtonContent(int resourceId,OnClickListener onClickListener){
        if (mTitleRootView != null){
            rl_right_group.setVisibility(View.VISIBLE);
            iv_title_right_img.setImageResource(resourceId);
            iv_title_right_img.setVisibility(View.VISIBLE);
            tv_title_right_text.setVisibility(View.GONE);

            rl_right_group.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置右上角文字
     */
    public void setRightButtonContent(CharSequence backText,OnClickListener onClickListener){
        if (mTitleRootView != null){
            rl_right_group.setVisibility(View.VISIBLE);
            tv_title_right_text.setText(backText);
            tv_title_right_text.setVisibility(View.VISIBLE);
            iv_title_right_img.setVisibility(View.GONE);

            rl_right_group.setOnClickListener(onClickListener);
        }
    }

    /**
     * TitleBarLayout样式
     */
    public enum Style {
        //红色主体样式
        RED(0x0),
        //白色主体样式
        WHITE(0x1);


        static Style mapIntToValue(final int modeInt) {
            for (Style value : Style.values()) {
                if (modeInt == value.getIntValue()) {
                    return value;
                }
            }

            // If not, return default
            return getDefault();
        }

        static Style getDefault() {
            return RED;
        }

        private int mIntValue;

        // The modeInt values need to match those from attrs.xml
        Style(int modeInt) {
            mIntValue = modeInt;
        }


        int getIntValue() {
            return mIntValue;
        }

    }


}
