package com.yao.dependence.widget.feedlist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.yao.dependence.R;
import com.yao.dependence.widget.feedlist.adapter.WrapperRecyclerViewAdapter;
import com.yao.dependence.widget.feedlist.loadmore.CommonLoadMoreView;
import com.yao.dependence.widget.recycler.divider.DividerItemDecoration;
import com.yao.dependence.widget.recycler.listener.OnRcvScrollListener;
import com.yao.devsdk.adapter.AceRecyclerAdapter;
import com.yao.devsdk.constants.SdkConst;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * RecyclerView的FeedList
 * Created by huichuan on 16/6/19.
 */
public class FeedRecyclerView<T> extends FeedListBase<T> implements FeedListBase.ISubOperate<T> {

    private RecyclerView mRecyclerView;
    private WrapperRecyclerViewAdapter<T,? extends RecyclerView.ViewHolder> adapter;

    public FeedRecyclerView(Context context) {
        super(context);
    }

    public FeedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }




    @Override
    public void inflateRootView() {
        View.inflate(mContext, R.layout.layout_feed_recycler_view, this);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_feed_list);

        setRecyclerViewLayoutManager();

        mRecyclerView.addOnScrollListener(new OnRcvScrollListener() {

            @Override
            public void onBottom() {

                loadMore();
            }
        });

    }

    @Override
    public PtrClassicFrameLayout findPullToRefreshView() {

        PtrClassicFrameLayout ptr_mainFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_mainFrame);
        return ptr_mainFrame;
    }

    /**
     * 列表的Adapter，务必调用
     */
    @SuppressWarnings("unchecked")
    @Override
    public FeedRecyclerView<T> setAdapter(Object recyclerAdapter) {
        if (!(recyclerAdapter instanceof AceRecyclerAdapter)){
            throw new RuntimeException("the FeedRecyclerView adapter should be sub class of AceRecyclerAdapter");
        }
        AceRecyclerAdapter<T,? extends RecyclerView.ViewHolder> innerAdapter = (AceRecyclerAdapter<T, ? extends RecyclerView.ViewHolder>) recyclerAdapter;
        this.adapter = new WrapperRecyclerViewAdapter(innerAdapter);

        //FooterView
        CommonLoadMoreView footerView = createLoadMoreView();
        loadMoreView = footerView;
        this.adapter.addFootView(footerView);
        mRecyclerView.setAdapter(adapter);
        return this;
    }


    CommonLoadMoreView createLoadMoreView(){
        CommonLoadMoreView footerView = new CommonLoadMoreView(mContext);
        if (SdkConst.DEBUG){
//            footerView.setBackgroundColor(Color.BLUE);
        }
        RecyclerView.LayoutParams footerLayoutParams = new RecyclerView.LayoutParams(-1,-2);
        footerView.setLayoutParams(footerLayoutParams);

        footerView.setOnClickModeListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
            }
        });

        return footerView;
    }

    /**增加Header头*/
    public FeedRecyclerView<T> addHeaderView(View header){
        adapter.addHeaderView(header);
        return this;
    }


    /**
     * loadingView的marginTop
     * @param marginTop
     * @return
     */
    public FeedRecyclerView<T> setLoadingViewMarginTop(int marginTop){
        loadingView.setMarginTop(marginTop);
        return this;
    }

    @Override
    public void addAdapterData(int index, List<T> list) {
        adapter.addAll(index,list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addAdapterData(List<T> list) {
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setAdapterData(List<T> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearAdapterData() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getAdapterListSize() {

        return adapter.getCount();
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     */
    public void setRecyclerViewLayoutManager() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST);

        mRecyclerView.addItemDecoration(itemDecoration);


    }



    public RecyclerView getListView() {
        return mRecyclerView;
    }



}
