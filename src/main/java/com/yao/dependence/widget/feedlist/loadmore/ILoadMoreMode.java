package com.yao.dependence.widget.feedlist.loadmore;

import android.view.View;

/**
 * Created by huichuan on 16/8/7.
 */
public interface ILoadMoreMode {
    //设置加载中文字
    void setText(int strId);

    // 加载中状态
    void setLoadingMode();

    // 正常状态,显示更多
    void setNormalMode();

    // 网络没有数据状态
    void setNoDataMode();

    void show();

    void hide();

    //该LoadMoreMode的listener
    void setOnClickModeListener(View.OnClickListener l);
}
