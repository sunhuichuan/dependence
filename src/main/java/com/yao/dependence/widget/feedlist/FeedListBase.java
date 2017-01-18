package com.yao.dependence.widget.feedlist;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.yao.dependence.R;
import com.yao.dependence.widget.feedlist.loadmore.ILoadMoreMode;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.net.exception.ApiException;
import com.yao.devsdk.utils.SdkUtil;

import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 可以把此类直接改成只有RecyclerView实现
 * @param <T>
 */
public abstract class FeedListBase<T> extends FrameLayout {
    private static final String TAG = "FeedListBase";

    protected Context mContext;

    /**
     * 普通状态
     */
    public static final int STATUS_IDLE = 0;//
    /**
     * 开始加载，对应的是{@link #startLoadData()};
     */
    public static final int STATUS_LOADING_START = 1;
    /**
     * 下拉加载，对应的是{@link #pullToLoad()}
     */
    public static final int STATUS_LOADING_FROM_TOP = 2;
    /**
     * 上拉加载，对应的是{@link #loadMore()}
     */
    public static final int STATUS_LOADING_FROM_BOTTOM = 3;
    /**
     * 下拉并重新加载，完成后会清空以前的数据，对应的是{@link #pullToReload()}
     */
    public static final int STATUS_LOADING_FROM_TOP_RELOAD = 4;


    /**
     * 是否拦截touch事件
     * true:拦截touch事件自己处理，false:不拦截，让子类处理
     */
    private boolean isInterceptTouchEvent = false;

    public int listStatus = STATUS_IDLE;
    /**
     * loadingView应该是默认可见并且一开始就显示loading的
     */
    protected LoadingInterface loadingView;
    private FeedListCallbacks callbacks;


    private PtrClassicFrameLayout pullToRefreshListView;


    protected ILoadMoreMode loadMoreView;

    private boolean canPullToLoad = false;
    private boolean canLoadMore = false;
    /**
     * 内部暂时决定是否可以加载更多和下拉刷新，但仅限原参数为true的情况下
     * canLoadMore这false时innerCanLoadMore不可以true
     * canPullToLoad这false时innerCanPullToLoad不可以true
     * //TODO 需要整理删除无效值
     */
    private boolean innerCanPullToLoad = false;
    private boolean innerCanLoadMore = false;
    protected boolean lastLoadMoreFailed = false;


    private boolean noMoreData = false;
    protected boolean autoLoadMore = true;//自动加载更多

//    private IFeedAdapter<T> mFeedAdapter;
    private ISubOperate<T> IMPL;
    private OnTouchListener mOnTouchListener;

    public FeedListBase(Context context) {
        super(context);
        initView(context);
    }

    public FeedListBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FeedListBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    @SuppressWarnings("unchecked")
    public void initView(Context context) {
        mContext = context;



        if (isInEditMode()){
            View.inflate(mContext, R.layout.layout_feed_recycler_view, this);
            return;
        }

        if (!(this instanceof ISubOperate)){
            throw new RuntimeException(" please implements the interface FeedListBase.IViewCreator ");
        }


//        pullToRefreshListView = (PtrClassicFrameLayout) findViewById(R.id.ptr_mainFrame);
//        loadingView = (LoadingInterface) findViewById(R.id.feed_loading_view);

        IMPL = (ISubOperate) this;
        IMPL.inflateRootView();
        pullToRefreshListView = IMPL.findPullToRefreshView();
        loadingView = createLoadingView();
        addView(loadingView);


//        loadMoreView = new CommonLoadMoreView(mContext);
//        loadMoreView.setOnClickModeListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadMore();
//            }
//        });

        pullToRefreshListView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis()
                        , DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                pullToRefreshListView.setLastUpdatedLabel(label);
                pullToRefreshListView.setLastUpdateTimeKey(label);
                if(isPullToReload()){
                    //重新加载
                    pullToReload();
                }else{
                    //下拉刷新
                    pullToLoad();
                }
            }
        });

    }


    /**
     * 创建LoadingView
     * @return
     */
    LoadingInterface createLoadingView(){
        LoadingInterface loadingView = new LoadingInterface(mContext);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1,-1);
//        params.gravity = Gravity.CENTER;
        return loadingView;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnTouchListener!=null){
            mOnTouchListener.onTouch(this,ev);
        }
        if (isInterceptTouchEvent){
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setInterceptTouchEvent(boolean isInterceptTouchEvent) {
        this.isInterceptTouchEvent = isInterceptTouchEvent;
    }


    @Override
    public void setOnTouchListener(OnTouchListener listener){
        this.mOnTouchListener = listener;
    }


//    /**
//     * 列表的Adapter，务必调用
//     */
//    public FeedListBase setAdapter(IFeedAdapter<T> adapter){
//        this.mFeedAdapter = adapter;
//        return this;
//    }





    /**
     * 设置发起网络请求的回调
     */
    public FeedListBase setCallbacks(FeedListCallbacks callbacks) {
        this.callbacks = callbacks;
        return this;
    }


    /**
     * 设置loadingView的view xml
     *
     * @param layout_id LoadingView对应的layout id
     */
    public FeedListBase setLoadingView(int layout_id) {
        setLoadingView(layout_id, -1);
        return this;
    }
    /**
     * 设置loadingView的view xml
     *
     * @param layout_id LoadingView对应的layout id
     */
    public FeedListBase setLoadingView(int layout_id,int marginTop) {
        loadingView.init(layout_id, marginTop);
        return this;
    }

    public void replaceLoadingView(int layout_id) {
        replaceLoadingView(layout_id, -1);
    }
    /**
     * 重置loadingView的view xml
     *
     * @param layout_id LoadingView对应的layout id
     * @param marginTop -1则代表不设置marginTop,维持layout_id布局中的原状
     */
    public void replaceLoadingView(int layout_id,int marginTop) {
        loadingView.init(layout_id, marginTop);
    }


    public FeedListBase<T> setLoadingListener(LoadingInterface.LoadingListener listener) {
        loadingView.setLoadingListener(listener);
        return this;
    }

    /**
     * 恢复上下拉加载
     */
    public void restoreRequestCondition() {
        setInnerCanPullToLoad(canPullToLoad);
        setInnerCanLoadMore(canLoadMore);
    }

    /**
     * 暂时阻止上下拉加载的请求
     */
    public void blockRequestTemporarily() {
        setInnerCanPullToLoad(false);
        setInnerCanLoadMore(false);
    }

    /**
     * 设置是否自动加载更多
     */
    public FeedListBase setAutoLoadMore(boolean autoLoadMore){
        this.autoLoadMore = autoLoadMore;
        return this;
    }

    /**
     * 设置是否可以上拉加载更多}
     */
    public FeedListBase setCanLoadMore(boolean flag) {
        canLoadMore = flag;
        innerCanLoadMore = flag;
        if (loadingView != null) {
            if (canLoadMore) {
                loadMoreView.show();
            } else {
                loadMoreView.hide();
            }
        } else {
            throw new Error("Call setLoadMoreView please");
        }
        return this;
    }

    public boolean getCanLoadMore() {
        return canLoadMore;
    }

    /**
     * 设置是否可以下拉加载，请在init之后调用
     */
    public FeedListBase setCanPullToLoad(boolean flag) {
        canPullToLoad = flag;
        innerCanPullToLoad = flag;
        pullToRefreshListView.setPullToRefresh(innerCanPullToLoad);
        pullToRefreshListView.setEnabled(innerCanPullToLoad);
//        pullToRefreshListView.setPullToRefreshEnabled(innerCanPullToLoad);
        if (innerCanLoadMore && pullToRefreshListView.isPullToRefresh()) {
//        if (innerCanLoadMore && pullToRefreshListView.isRefreshing()) {
//            pullToRefreshListView.onRefreshComplete();
            pullToRefreshListView.refreshComplete();
        }
        return this;
    }

    public boolean getCanPullToLoad() {
        return canPullToLoad;
    }

    /**
     * 用于在加载中时屏蔽其他加载操作，比如下拉时不能上拉刷新，请求完成后会自动复位
     */
    public void setInnerCanLoadMore(boolean icLoadMore) {
        if (canLoadMore) {
            innerCanLoadMore = icLoadMore;
            if (innerCanLoadMore) {
                loadMoreView.show();
            } else {
                loadMoreView.hide();
            }
        }
    }


    /**
     * 用于在加载中时屏蔽其他加载操作，比如上拉时不能下拉刷新，请求完成后会自动复位
     */
    public void setInnerCanPullToLoad(boolean icPullToLoad) {
        if (canPullToLoad) {
            innerCanPullToLoad = icPullToLoad;
            pullToRefreshListView.setPullToRefresh(innerCanPullToLoad);
//            pullToRefreshListView.setPullToRefreshEnabled(innerCanPullToLoad);
            if (innerCanLoadMore && pullToRefreshListView.isPullToRefresh()) {
//            if (innerCanLoadMore && pullToRefreshListView.isRefreshing()) {
                pullToRefreshListView.refreshComplete();
//                pullToRefreshListView.onRefreshComplete();
            }
        }
    }

    /**
     * 是否是pullToReload
     * @return
     */
    boolean isPullToReload(){
        return callbacks.getPullToReLoadParam() != null;
    }

    /**
     * 发起请求
     */
    public void invokeRequest(RequestType requestType) {
        HashMap<String, String> params = null;
        switch (requestType){
            case TYPE_START_LOAD:
                params = callbacks.getStartLoadParam();
                break;
            case TYPE_LOAD_NEW:
                params = callbacks.getPullToLoadParam();
                break;
            case TYPE_LOAD_MORE:
                params = callbacks.getLoadMoreParam();
                break;
            case TYPE_PULL_TO_RELOAD:
                params = callbacks.getPullToReLoadParam();
                break;
            default:
                SdkUtil.showDebugToast(SdkConfig.getAppContext(),"不合法请求类型");
                break;
        }
        callbacks.startRequestFeed(requestType,params);
    }

    /**
     * 默认清除数据
     */
    public void reset() {
        reset(true);
    }
    /**
     * 清除数据，恢复状态，重置
     */
    public void reset(boolean isClearData) {
        noMoreData = false;
        callbacks.abortRequestFeed();
        onLoadFinish();
        if (isClearData){
            //是否清除数据
            IMPL.clearAdapterData();

        }
        if (canLoadMore) {
            loadMoreView.setNormalMode();
        }
    }

//    /**
//     * 不清除数据，恢复状态，重置
//     */
//    public void restore() {
//        noMoreData = false;
//        callbacks.abortRequestFeed();
//        onLoadFinish();
//        if (canLoadMore) {
//            loadMoreView.setNormalMode();
//        }
//    }

    /**
     * 加载列表数据，自动根据列表中是否有数据决定加载方式。
     * 如果已经有了数据，那么执行{@link #pullToReload()}，如果还是空表，那么执行{@link #startLoadData()}
     * //TODO 是否应该都是清空adapter，重新加载数据？
     */
    public void reloadFeedList() {
        if (IMPL.getAdapterListSize() > 0) {
            pullToReload();
        } else {
            startLoadData();
        }
    }

    /**
     * 开始加载数据，用于列表为空时加载，或者自行在执行前/后清空列表
     * 本方法不负责清空列表，但是在加载完成后会清空原有列表（如果有的话）……
     * 因此如果列表中已经有了数据，要重新加载数据，要么调用{@link #pullToReload()}，要么在调用此方法前清空列表。
     * 如果不想做是否清空的判断，也不想执行清空，那么直接调用{@link #reloadFeedList()}
     *
     */
    public void startLoadData() {
        noMoreData = false;
        if (listStatus == STATUS_IDLE) {
            blockRequestTemporarily();
            listStatus = STATUS_LOADING_START;
            if (IMPL.getAdapterListSize() == 0) {
                loadingView.startLoading();
            }

            invokeRequest(RequestType.TYPE_START_LOAD);
        }
    }

    /**
     * 下拉并重新加载，用于列表中已经有数据的情况，加载完全后替换所有数据,与StartLoad作用相同。非手动下拉触发.
     * 如果不想做是否有数据的判断，那么直接调用{@link #reloadFeedList()}
     */
    public void pullToReload() {
//        if (canPullToLoad && innerCanPullToLoad && listStatus == STATUS_IDLE) {
//            setInnerCanLoadMore(false);
//            pullToRefreshListView.setRefreshing();
//            listStatus = STATUS_LOADING_FROM_TOP_RELOAD;
//            invokeRequest(callbacks.getStartLoadParam());
//        }
        if (listStatus == STATUS_IDLE) {
            if (canPullToLoad && innerCanPullToLoad) {
                //可以下拉刷新
//                pullToRefreshListView.setRefreshing();
                //TODO 因为库改了，这里逻辑可能要改
                pullToRefreshListView.autoRefresh();
            }
            setInnerCanLoadMore(false);
            listStatus = STATUS_LOADING_FROM_TOP_RELOAD;


            invokeRequest(RequestType.TYPE_START_LOAD);
        }
    }

    /**
     * 下拉加载.
     * 事实上最理想的触发方式是完全经由PullToRefreshListView来决定，这样就不必出什么状态问题。
     * 手动下拉加载由PullToRefreshListView通过回调触发.其他的下拉加载，也应该如此。
     * 比如每隔一小时自动刷新一次，也应该由PullToRefreshListView决定才是好的——先执行下拉动画再走刷新的回调
     * 但是PullToRefreshListView貌似不是这样，如此就会有两套状态管理，这里一个，PullToRefreshListView内部一个
     */
    public void pullToLoad() {
//        if (canPullToLoad && innerCanPullToLoad && listStatus == STATUS_IDLE) {
//            setInnerCanLoadMore(false);
//            pullToRefreshListView.setRefreshing();
//            listStatus = STATUS_LOADING_FROM_TOP;
//            invokeRequest(callbacks.getPullToLoadParam());
//        }

        if (listStatus == STATUS_IDLE){
            if (canPullToLoad && innerCanPullToLoad){
                //可以支持下拉刷新
//                pullToRefreshListView.setRefreshing();
                //TODO 因为库改了，这里逻辑可能要改
                pullToRefreshListView.autoRefresh();
            }
            setInnerCanLoadMore(false);
            listStatus = STATUS_LOADING_FROM_TOP;


            invokeRequest(RequestType.TYPE_LOAD_NEW);
        }

    }

    /**
     * 加载更多.
     */
    public void loadMore() {
        //事实上innerCanLoadMore的判断是不必要的，因为在listStatus == STATUS_IDLE的时候innerCanLoadMore==canLoadMore
        if (canLoadMore && innerCanLoadMore && listStatus == STATUS_IDLE) {
            setInnerCanPullToLoad(false);
            loadMoreView.setLoadingMode();
            listStatus = STATUS_LOADING_FROM_BOTTOM;
            invokeRequest(RequestType.TYPE_LOAD_MORE);
        }
    }

    /**
     * 获取请求到的数据。
     * 数据不是Json，而是已经转成ArrayList的数据。
     * 请求失败或者转Json失败的，会走{@link #onLoadDataError(FeedListBase.RequestType,Throwable)}
     */
    public void onLoadDataOK(RequestType requestType,List<T> list) {

        int listSize = (list==null)?0:list.size();
        noMoreData =  (listSize == 0) ;
        if (requestType == RequestType.TYPE_LOAD_NEW) {
            IMPL.addAdapterData(0, list);
        } else if (requestType == RequestType.TYPE_LOAD_MORE) {
            IMPL.addAdapterData(list);
            lastLoadMoreFailed = (listSize == 0);
        } else {
            //TYPE_START_LOAD
            //TYPE_RELOAD

            if (listSize == 0) {
                IMPL.clearAdapterData();
                loadingView.onNoData();
            } else {
                IMPL.setAdapterData(list);
                loadingView.onLoadSuccess();
            }
        }
        onLoadFinish();
    }


    /**
     * 加载数据失败，包括网络失败、服务器错误、parse错误等
     */
    public void onLoadDataError(final RequestType requestType,Throwable throwable) {
        //没有联网的话，增加响应200时间
        final boolean networkConnected = SdkUtil.isNetworkConnected(getContext());
        loadDataError(requestType, networkConnected,throwable);

    }


    private void loadDataError(RequestType requestType, boolean networkConnected,Throwable throwable) {
        if (IMPL.getAdapterListSize() == 0) {
            if (networkConnected) {
                //有网，但是Adapter无内容，把 错误提示 改为 空白提示 吧
//                loadingView.onLoadFailed();
                loadingView.onNoData();
            } else {
                loadingView.onNoNet();
            }
        }else{
            //adapter不为空
            if(throwable!=null && throwable instanceof ApiException){
                ApiException apiEx = (ApiException) throwable;
                noMoreData = apiEx.isErrorNoData();
            }

        }
        if (requestType == RequestType.TYPE_LOAD_MORE) {
            lastLoadMoreFailed = true;
        }
        onLoadFinish();
    }


    /**
     * 直接设置数据，没有经过网络请求
     */
    public void setData(List<T> list) {
        if (listStatus == STATUS_IDLE) {
            IMPL.setAdapterData(list);
            loadingView.onLoadSuccess();
        }
    }

    /**
     * 加载完成，与成功失败无关，作用是复位顶部或者底部的View
     */
    private void onLoadFinish() {
        if (IMPL.getAdapterListSize() == 0) {
            setInnerCanLoadMore(false);
            setInnerCanPullToLoad(false);
        } else {
            if (listStatus ==  STATUS_LOADING_FROM_TOP || listStatus == STATUS_LOADING_FROM_TOP_RELOAD) {
//                pullToRefreshListView.onRefreshComplete();
                pullToRefreshListView.refreshComplete();
            } else if (listStatus == STATUS_LOADING_FROM_BOTTOM) {
                loadMoreView.setNormalMode();
            }
            restoreRequestCondition();
        }
        if (noMoreData) {
            loadMoreView.setNoDataMode();
        }
        listStatus = STATUS_IDLE;

    }




    public PtrClassicFrameLayout getPullToRefreshListView() {
        return pullToRefreshListView;
    }


    /**
     * 请求的类型
     * 不同的请求类型对应不同的参数组合
     */
    public enum RequestType{

        /**
         * feed流为空第一次加载
         */
        TYPE_START_LOAD,
        /**
         * 流不为空，下拉加载新内容
         */
        TYPE_LOAD_NEW,
        /**
         * 流不为空，加载更多
         */
        TYPE_LOAD_MORE,
        /**
         * 流不为空，重新覆盖更新
         */
        TYPE_PULL_TO_RELOAD;

    }


    /**
     * 一个简单的LoadingListener实现类
     */
    public static class SimpleLoadingListener implements LoadingInterface.LoadingListener{

        private FeedListBase feedListBase;

        public SimpleLoadingListener(FeedListBase feedListBase){
            this.feedListBase = feedListBase;
        }

        @Override
        public void onErrorViewClicked() {
            feedListBase.reloadFeedList();
        }

        @Override
        public void onNoDataViewClicked() {
            feedListBase.reloadFeedList();
        }

        @Override
        public void onNoNetViewClicked() {
            feedListBase.reloadFeedList();
        }
    }



    /**
     * FeedList通过此回调调用外界的方法
     */
    public interface FeedListCallbacks {

        HashMap<String, String> getStartLoadParam();

        HashMap<String, String> getPullToLoadParam();

        HashMap<String, String> getLoadMoreParam();

        HashMap<String, String> getPullToReLoadParam();

        /**
         * 因为当前上下拉和从头加载用的都是同一个请求，所以最终请求使用同一个出口。
         * 加载数据完成（无论成功失败）后，外部请务必调用FeedList的{@link #onLoadDataOK(FeedListBase.RequestType,List)}
         * 或者{@link #onLoadDataError(FeedListBase.RequestType,Throwable)}，靠自觉……
         */
        void startRequestFeed(RequestType requestType, HashMap<String, String> params);

        /**
         * 取消所有正在进行的网络请求
         */
        void abortRequestFeed();
    }


    /**
     * View 创建的creator
     */
    public interface ISubOperate<T>{

        /**
         * 用xml填充RootView
         */
        void inflateRootView();
        /**
         * 找到下拉刷新View
         */
        PtrClassicFrameLayout findPullToRefreshView();

        /**设置Adapter*/
        FeedListBase setAdapter(Object adapter);
        void addAdapterData(int index,List<T> list);
        void addAdapterData(List<T> list);
        void setAdapterData(List<T> list);
        void clearAdapterData();
        int getAdapterListSize();

    }

}
