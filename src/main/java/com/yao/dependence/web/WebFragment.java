package com.yao.dependence.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yao.dependence.R;
import com.yao.dependence.ui.BaseFragment;
import com.yao.dependence.utils.URICenter;
import com.yao.devsdk.SdkConfig;
import com.yao.devsdk.log.LoggerUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WebFragment extends BaseFragment {


    protected static final String TAG = "InnerBrowserTestActivity";
    public static final int API = android.os.Build.VERSION.SDK_INT;

    /**
     * 同一个url加载失败次数，超过3次就不再加载
     */
    private Map<String,Integer> mUrlLoadErrorMap = new HashMap<>();

    Context appContext;
    public static final String ARGS_TITLE = "title";
    public static final String ARGS_URL = "url";

    /**
     * 是否显示page中间的进度条
     */
    protected boolean isShowPageCenterProgressBar = true;
    protected WebView mWebShow;
    protected LinearLayout ll_bottomToolBar;
    protected ProgressBar mProgressBar, pb_pageCenterProgressBar;
    private View mBack, mNext, mRefresh, mHomePage, mMore;
    private String originTitle;
    private String originUrl;

    private String currentUrl;
    private String redirectUrl;
//    private int mDensity;
    private int index;


    private OnFragmentInteractionListener mListener;

    public WebFragment() {
    }

    public static WebFragment newInstance(String title, String url) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_TITLE, title);
        args.putString(ARGS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            originTitle = getArguments().getString(ARGS_TITLE);
            originUrl = getArguments().getString(ARGS_URL);
            if (!TextUtils.isEmpty(originUrl)){
                if (!originUrl.startsWith("http://")){
                    originUrl = "http://"+originUrl;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
        initData();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        appContext = SdkConfig.getAppContext();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    private void initLayout(View rootView) {
        mWebShow = (WebView) rootView.findViewById(R.id.wvWebShow);

        ll_bottomToolBar = (LinearLayout) rootView.findViewById(R.id.ll_bottomToolBar);
        mBack = rootView.findViewById(R.id.mMenuBack);
        mNext = rootView.findViewById(R.id.mMenuNext);
        mRefresh = rootView.findViewById(R.id.mMenuRefresh);
        mHomePage = rootView.findViewById(R.id.mMenuHomePage);
        mMore = rootView.findViewById(R.id.mMenuMore);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.WebViewProgress);
        mProgressBar.setMax(100);
        pb_pageCenterProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_pageCenterProgressBar);
        if (!isShowPageCenterProgressBar){
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }


    private void initData() {

        //TODO 种cookie的操作
//        if(SdkConfig.isLogin()) {
//            CookieTask.getInstance(thisContext).setAndSyncCookie();
//            CookieSyncManager.getInstance().sync();
//        }else{
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.removeAllCookie();
//        }


        currentUrl = originUrl;
        mWebShow.setWebChromeClient(new CustomChromeClient());
        mWebShow.setWebViewClient(new CustomWebViewClient());
        BrowserSettings(mWebShow);
        mWebShow.loadUrl(currentUrl);

        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebShow.canGoBack()) {
                    if (mWebShow.getProgress() < 100) {
                        mWebShow.stopLoading();
                    }
                    mWebShow.goBack();
//                    mNext.setImageResource(R.drawable.toolbar_advance);
                } else {
//                    Uri uri = Uri.EMPTY;
//                    Uri.Builder builder = uri.buildUpon();

                    mListener.onFragmentInteraction(URICenter.FINISH_ACTIVITY);//finish();
                }
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebShow.canGoForward()) {
                    redirectUrl = null;
                    mWebShow.goForward();
                    if (mWebShow.canGoForward()) {
//                        mNext.setImageResource(R.drawable.toolbar_advance);
                    }
                } else {
//                    mNext.setImageResource(R.drawable.toolbar_advance);
                }
            }
        });
        mRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebShow.getProgress() < 100) {
                    mWebShow.stopLoading();
                } else {
                    mWebShow.reload();
                }

            }
        });
        mHomePage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onFragmentInteraction(URICenter.FINISH_ACTIVITY);//finish();
                }
            }
        });
        mMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 重新加载此页面
     */
    public void reloadPage(){
        mWebShow.setVisibility(View.VISIBLE);
        mWebShow.reload();
    }



    /**
     * 用户的返回事件
     * @return true 返回事件被消费，Activity 不 finish
     */
    public boolean onBackPressed(){
        if (mWebShow.canGoBack()) {
            if (mWebShow.getProgress() < 100) {
                mWebShow.stopLoading();
            }
            mWebShow.goBack();
//                mNext.setImageResource(R.drawable.toolbar_advance);
            return true;
        }
//        else {
//            mListener.onFragmentInteraction(UriCenter.FINISH_ACTIVITY);//finish();
//        }
        return false;
    }


    @Override
    public void onStop() {
        super.onStop();
        mWebShow.freeMemory();
    }


    private void checkWebViewUrl(final WebView webView, final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... params) {
                int responseCode = -1;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    responseCode = connection.getResponseCode();
                } catch (Exception e) {
                }
                return responseCode;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result != 200) {
                    //不等于200也不能gone啊
//                    webView.setVisibility(View.GONE);
                } else {
                    webView.loadUrl(url);
                }
            }
        }.execute(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public class CustomChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public Bitmap getDefaultVideoPoster() {
            // Log.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (mDefaultVideoPoster == null) {
                mDefaultVideoPoster = BitmapFactory.decodeResource(
                        getResources(), android.R.color.black);
            }
            return mDefaultVideoPoster;
        }

        @Override
        public View getVideoLoadingProgressView() {
            // Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(appContext);
                mVideoProgressView = inflater.inflate(
                        android.R.layout.simple_spinner_item, null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (isShowPageCenterProgressBar){
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setProgress(0);
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                                             WebStorage.QuotaUpdater quotaUpdater) {
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        }


        @Override
        public void onReceivedTitle(final WebView view, final String title) {
            if (!TextUtils.isEmpty(title)) {
//                InnerBrowserTestActivity.this.getActionBar().setTitle(title);
                if (mListener!=null){
                    mListener.onFragmentInteraction(URICenter.SET_TITLE);
                }
            }
            super.onReceivedTitle(view, title);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    public WebView BrowserSettings(WebView view) {
        //view.setAnimationCacheEnabled(false);
        //view.setDrawingCacheEnabled(false);
        //view.setDrawingCacheBackgroundColor(getResources().getColor(
                //android.R.color.background_light));
        // view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // view.setAlwaysDrawnWithCacheEnabled(true);
        //view.setWillNotCacheDrawing(true);
        //view.setFocusable(true);
        //view.setFocusableInTouchMode(true);
        //view.setSaveEnabled(true);

        WebSettings webViewSettings = view.getSettings();

        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setAllowFileAccess(false);
        webViewSettings.setLightTouchEnabled(true);
        webViewSettings.setSupportMultipleWindows(false);
        webViewSettings.setDomStorageEnabled(true);
        webViewSettings.setAppCacheEnabled(false);
        webViewSettings.setAppCachePath(appContext.getFilesDir()
                .getAbsolutePath() + "/cache");
        webViewSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webViewSettings.setGeolocationEnabled(true);
        webViewSettings.setGeolocationDatabasePath(appContext
                .getFilesDir().getAbsolutePath());
        webViewSettings.setDatabaseEnabled(false);
        webViewSettings.setDatabasePath(appContext.getFilesDir()
                .getAbsolutePath() + "/databases");
//        WebView test = new WebView(appContext); // getting default webview
//
//        // user agent
//        String userAgent = test.getSettings().getUserAgentString();

        String ua = SdkConfig.getCurrentUserAgent(appContext);
        String uaNew = webViewSettings.getUserAgentString() + ua;
        webViewSettings.setUserAgentString(uaNew);

        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setSupportZoom(true);
        webViewSettings.setUseWideViewPort(true);
        webViewSettings.setLoadWithOverviewMode(true); // Seems to be causing
        // the performance
        // to drop
        if (API >= 11) {
            webViewSettings.setDisplayZoomControls(false);
            webViewSettings.setAllowContentAccess(true);
        }
        webViewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webViewSettings.setLoadsImagesAutomatically(true);
        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        return view;
    }

    public class CustomWebViewClient extends WebViewClient {


        @Override
        public void doUpdateVisitedHistory(WebView view, final String url,
                                           final boolean isReload) {

        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            if (isShowPageCenterProgressBar){
                pb_pageCenterProgressBar.setVisibility(View.INVISIBLE);
            }
            // view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            LoggerUtil.i(TAG, "页面加载完成");
            String title = mWebShow.getTitle();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (isShowPageCenterProgressBar){
                pb_pageCenterProgressBar.setVisibility(View.VISIBLE);
            }

            currentUrl = url;
            LoggerUtil.i(TAG, "开始加载页面");

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            if (url.startsWith("mailto:") || url.startsWith("geo:")
                    || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                view.goBack();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                                              HttpAuthHandler handler, String host, String realm) {
            // handler.proceed(username, password);
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm,
                                           String account, String args) {

                super.onReceivedLoginRequest(view, realm, account, args);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {

            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {

            view.getSettings().setLayoutAlgorithm(
                    WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            if (!isOverRetryCount(failingUrl)){
                checkWebViewUrl(view, failingUrl);
            }
        }

    }


    boolean isOverRetryCount(String failingUrl){
        Integer count = mUrlLoadErrorMap.get(failingUrl);
        if (count == null){
            count = 0;
        }
        //判断是否到重试上限
        if (count<3){
            count++;
            mUrlLoadErrorMap.put(failingUrl, count);
            return false;
        }

        return true;
    }










    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    //TODO 这个接口可以废弃掉，因为SDKBaseFragment中有此接口的实现
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
