package com.yao.dependence.widget.feedlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.yao.dependence.R;
import com.yao.devsdk.adapter.AceAdapter;
import com.yao.devsdk.imageloader.ImageLoaderManager;
import com.yao.devsdk.utils.DisplayUtil;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * ListView的FeedList
 * Created by huichuan on 16/6/19.
 *
 * 此类不再维护，请勿调用，以后还会被删除
 */

@Deprecated
public class FeedListView<T> extends FeedListBase<T> implements FeedListBase.ISubOperate {

    private ListView listView;
    private AceAdapter<T> adapter;
    private View headerView;
    private AbsListView.OnScrollListener extOnScrollListener;
    private int lastScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private PauseOnScrollListener pauseOnScrollListener;


    public FeedListView(Context context) {
        super(context);
    }

    public FeedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    public View getHeader() {
        return headerView;
    }

    public void setHeader(View view) {
        removeHeader();
        headerView = view;
        listView.addHeaderView(view);
    }

    public void removeHeader() {
        if (headerView != null) {
            listView.removeHeaderView(headerView);
            headerView = null;
        }
    }



    public ListView getListView() {
        return listView;
    }


    @Override
    public void inflateRootView() {
        View.inflate(mContext, R.layout.layout_feed_list_view, this);

        pauseOnScrollListener = new PauseOnScrollListener(ImageLoaderManager.getInstance().getUILInstance(), false, true);

//        listView.addFooterView(loadMoreView);

        //去掉divider
//        listView.setDivider(HeadlineApplication.getApplication().getResources().getDrawable(R.drawable.feed_item_divider));
//        listView.setDividerHeight(0);
    }

    @Override
    public PtrClassicFrameLayout findPullToRefreshView() {

        PtrClassicFrameLayout ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_mainFrame);

        return ptrClassicFrameLayout;
    }

    /**
     * 列表的Adapter，务必调用
     */
    @SuppressWarnings("unchecked")
    @Override
    public FeedListBase setAdapter(Object adapter) {
        if (!(adapter instanceof AceAdapter)) {
            throw new RuntimeException("the adapter should be sub class of AceAdapter ");
        }
        AceAdapter<T> aceAdapter = (AceAdapter<T>) adapter;
        this.adapter = aceAdapter;
        //为了避免4.4以下系统setAdapter后再addHeader报异常做的兼容处理
        View placeHolderView = new View(mContext);
        listView.addHeaderView(placeHolderView);
        listView.setAdapter(aceAdapter);
        listView.removeHeaderView(placeHolderView);
        return this;
    }

    @Override
    public void addAdapterData(int index, List list) {
        adapter.addAll(index,list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addAdapterData(List list) {
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setAdapterData(List list) {
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


    public void setExtOnScrollListener(AbsListView.OnScrollListener extOnScrollListener) {
        this.extOnScrollListener = extOnScrollListener;
    }





    /**ListView滚动监听*/
    class WrapperScrollListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
//        LogPrinter.e(TagUtils.TEST, "onScrollStateChanged()");
            if (extOnScrollListener != null) {
                extOnScrollListener.onScrollStateChanged(view, scrollState);
            }
            if (pauseOnScrollListener != null) {
                pauseOnScrollListener.onScrollStateChanged(view, scrollState);
            }
            if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                lastScrollState = SCROLL_STATE_TOUCH_SCROLL;
                return;
            }
            if (lastScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                lastScrollState = scrollState;
                if (!autoLoadMore){
                    //不自动加载更多
                    return;
                }

            int firstVisibleItem = listView.getFirstVisiblePosition();
            int lastVisibleItem = listView.getLastVisiblePosition();
            int visibleItemCount = lastVisibleItem - firstVisibleItem + 1;
            int totalItemCount = adapter.getList().size();
            if (totalItemCount - firstVisibleItem - visibleItemCount <= 5 && totalItemCount > 5) {
                View footView = listView.getChildAt(listView.getChildCount() - 1);
                int[] location = {0, 0};
                footView.getLocationOnScreen(location);
                if (location[1] + footView.getHeight() < (DisplayUtil.getScreenHeight(getContext()) + 3)) {
                    loadMore();
                }
            }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        LogPrinter.e(TagUtils.TEST, "onScroll()");
            if (extOnScrollListener != null) {
                extOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            if (pauseOnScrollListener != null) {
                pauseOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            if (totalItemCount - firstVisibleItem - visibleItemCount > 5) {
                lastLoadMoreFailed = false;
            }
            if (!autoLoadMore){
                //不自动加载更多
                return;
            }
            if ((lastScrollState == SCROLL_STATE_TOUCH_SCROLL || lastScrollState == SCROLL_STATE_FLING)
                    && !lastLoadMoreFailed
                    && totalItemCount - firstVisibleItem - visibleItemCount < 5
                    && totalItemCount > 5) {
                loadMore();
            }
        }
    }





}
