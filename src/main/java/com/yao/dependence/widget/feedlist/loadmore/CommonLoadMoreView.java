package com.yao.dependence.widget.feedlist.loadmore;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yao.dependence.R;
import com.yao.devsdk.utils.SdkUtil;


public class CommonLoadMoreView extends RelativeLayout implements ILoadMoreMode{

    private TextView mTextView;
    private ProgressBar mProgressBar;

    String default_loadmore_text = "加载更多精彩内容";
    String default_loading_text = "加载中";
    String default_nodata_text = "暂时没有更多内容";

    public CommonLoadMoreView(Context context) {
        super(context);
        setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(-1, -2);
        setLayoutParams(params);
        View.inflate(context, R.layout.layout_view_load_more, this);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_more_progress);
        mTextView = (TextView) findViewById(R.id.tv_more_text);
        setNormalMode();
    }

    @Override
    public void setText(int strId) {
        mTextView.setText(strId);
    }

    // 加载中状态
    @Override
    public void setLoadingMode() {
        mTextView.setText(default_loading_text);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    // 正常状态,显示更多
    @Override
    public void setNormalMode() {
        mTextView.setText(default_loadmore_text);
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    // 网络没有数据状态
    @Override
    public void setNoDataMode() {
        if (SdkUtil.isNetworkConnected(getContext())) {
            mTextView.setText(default_nodata_text);
        } else {
            mTextView.setText(default_loadmore_text);
        }
        mTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);//在ListView中，仅仅设置visibility是不够的，最好是直接在ListView删除或者高度变为0
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }

    //该LoadMoreMode的listener
    @Override
    public void setOnClickModeListener(OnClickListener l) {
        this.setOnClickListener(l);
    }

    public void setLoadMoreText(String text) {
        default_loadmore_text = text;
        mTextView.setText(default_loadmore_text);
    }

    public void setLoadingText(String text) {
        default_loading_text = text;
    }

    public void setNoDataText(String text) {
        default_nodata_text = text;
    }
}
