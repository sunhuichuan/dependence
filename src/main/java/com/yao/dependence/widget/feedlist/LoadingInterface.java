package com.yao.dependence.widget.feedlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yao.dependence.R;
import com.yao.devsdk.log.LogUtil;


/**
 * Feed流加载数据的抽象类，只在没有数据时才有的操作。
 * 不关心ListView显示与否、不关心是否登录。
 * noNetView其实完全可以用errorView代替，PM真无聊
 */
public class LoadingInterface extends RelativeLayout {

    private static final String TAG = "LoadingInterface";
    /**
     * 默认的loadingLayout
     */
    private final int DEFAULT_LOADING_LAYOUT = R.layout.layout_loading_feed;

    private Context mContext;

    View innerParent;
    View progressView;
    View errorView;
    View noDataView;
    View noNetView;
    LoadingListener reloadListener;

    public LoadingInterface(Context context) {
        this(context,null,0);
    }

    public LoadingInterface(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingInterface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(DEFAULT_LOADING_LAYOUT,-1);

        //防止LoadingView被穿透
        setClickable(true);
    }

    /**
     * 自觉使用对应id
     *
     * @param layout_id layout的id
     * @param marginTop -1,代表不设置marginTop 遵循原layout
     */
    public void init(int layout_id,int marginTop) {
        removeAllViews();

        innerParent = View.inflate(getContext(), layout_id, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1,-1);
        if (marginTop > 0){
            //marginTop 大于0,再赋值
            params.topMargin = marginTop;
        }
        innerParent.setLayoutParams(params);
        addView(innerParent);



        progressView = findViewById(R.id.view_loading);
        errorView = findViewById(R.id.view_load_error);
        errorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reloadListener != null) {
                    reloadListener.onErrorViewClicked();
                }
            }
        });
        noDataView = findViewById(R.id.view_no_data);
        noDataView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reloadListener != null) {
                    reloadListener.onNoDataViewClicked();
                }
            }
        });
        noNetView = findViewById(R.id.view_no_net);
        noNetView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reloadListener != null) {
                    reloadListener.onNoNetViewClicked();
                }
            }
        });
    }

    /**
     * 设置loadingView的marginTop
     * @param marginTop
     */
    public void setMarginTop(int marginTop){
        if (innerParent == null){
            return;
        }
        RelativeLayout.LayoutParams params = (LayoutParams) innerParent.getLayoutParams();
        params.topMargin = marginTop;
        innerParent.setLayoutParams(params);
    }

    public void setLoadingListener(LoadingListener listener) {
        this.reloadListener = listener;
    }

    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }

    /**
     * 重新加载
     */
    public void reload() {
        startLoading();
    }

    /**
     * 开始加载数据
     */
    public void startLoading() {
        progressView.setVisibility(VISIBLE);
        errorView.setVisibility(GONE);
        noDataView.setVisibility(GONE);
        noNetView.setVisibility(GONE);
        setVisibility(VISIBLE);
    }

    /**
     * 首次加载失败时的操作
     */
    public void onLoadFailed() {
        progressView.setVisibility(GONE);
        errorView.setVisibility(VISIBLE);
        noDataView.setVisibility(GONE);
        noNetView.setVisibility(GONE);
    }

    /**
     * 返回数据为空时的操作
     */
    public void onNoData() {
        progressView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        noDataView.setVisibility(VISIBLE);
        noNetView.setVisibility(GONE);
    }

    /**
     * 返回无网络时的操作
     */
    public void onNoNet() {
        progressView.setVisibility(GONE);
        errorView.setVisibility(GONE);
        noDataView.setVisibility(GONE);
        noNetView.setVisibility(VISIBLE);
    }


    /**
     * 加载成功时的操作
     */
    public void onLoadSuccess() {
        setVisibility(GONE);
    }

    public interface LoadingListener {
        void onErrorViewClicked();

        void onNoDataViewClicked();

        void onNoNetViewClicked();
    }
}
