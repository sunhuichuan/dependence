package com.yao.dependence.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yao.dependence.R;
import com.yao.dependence.ui.BaseActivity;
import com.yao.devsdk.log.LogUtil;

import java.net.HttpURLConnection;
import java.net.URL;


public class InnerBrowserTestActivity extends BaseActivity {
	protected static final String TAG = "InnerBrowserTestActivity";
    public static final int API = Integer
            .valueOf(android.os.Build.VERSION.SDK_INT);
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String ADID = "adid";
    public static final String CREDIT = "credit";
    
    private WebView mWebShow;
    private ProgressBar mProgressBar, progressBar;
    private View mBack, mNext, mRefresh, mHomePage, mMore;
    private String defaultTitle;
    private String defaultUrl;
    private String currentUrl;
    private String redirectUrl;
    private int mDensity;
    private int index;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mDensity = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

        setContentView(R.layout.activity_brawser);

        initLayout();
        initData();
    }

    private void initLayout() {
        mWebShow = (WebView) findViewById(R.id.wvWebShow);
        mBack = findViewById(R.id.mMenuBack);
        mNext = findViewById(R.id.mMenuNext);
        mRefresh = findViewById(R.id.mMenuRefresh);
        mHomePage = findViewById(R.id.mMenuHomePage);
        mMore = findViewById(R.id.mMenuMore);
        mProgressBar = (ProgressBar) findViewById(R.id.WebViewProgress);
        mProgressBar.setMax(100);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        
    }


    private void initData() {
        defaultTitle = getIntent().getStringExtra(TITLE);
        defaultUrl = getIntent().getStringExtra(URL);
        currentUrl = defaultUrl;
        mWebShow.setWebChromeClient(new myChromeClient());
        mWebShow.setWebViewClient(new myWebViewClient());
        BrowserSettings(mWebShow);
        mWebShow.loadUrl(defaultUrl);

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
                    finish();
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
                finish();
            }
        });
        mMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebShow.canGoBack()) {
                if (mWebShow.getProgress() < 100) {
                    mWebShow.stopLoading();
                }
                mWebShow.goBack();
//                mNext.setImageResource(R.drawable.toolbar_advance);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

  

    @Override
    protected void onStop() {
        super.onStop();
        mWebShow.freeMemory();
    }


    private void checkWebViewUrl(final WebView webView, final String url) {
        if (url == null || url.equals("")) {
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
                    webView.setVisibility(View.GONE);
                } else {
                    webView.loadUrl(url);
                }
            }
        }.execute(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public class myChromeClient extends WebChromeClient {
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
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
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
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setProgress(0);
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
                QuotaUpdater quotaUpdater) {
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        }


        @Override
        public void onReceivedTitle(final WebView view, final String title) {
            if (title != null) {
                InnerBrowserTestActivity.this.getActionBar().setTitle(title);
            }
            super.onReceivedTitle(view, title);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    public WebView BrowserSettings(WebView view) {
        view.setAnimationCacheEnabled(false);
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheBackgroundColor(getResources().getColor(
                android.R.color.background_light));
        // view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // view.setAlwaysDrawnWithCacheEnabled(true);
        view.setWillNotCacheDrawing(true);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setSaveEnabled(true);

        WebSettings webViewSettings = view.getSettings();

        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setAllowFileAccess(true);
        webViewSettings.setLightTouchEnabled(true);
        webViewSettings.setSupportMultipleWindows(false);
        webViewSettings.setDomStorageEnabled(true);
        webViewSettings.setAppCacheEnabled(true);
        webViewSettings.setAppCachePath(getApplicationContext().getFilesDir()
                .getAbsolutePath() + "/cache");
        webViewSettings.setRenderPriority(RenderPriority.HIGH);
        webViewSettings.setGeolocationEnabled(true);
        webViewSettings.setGeolocationDatabasePath(getApplicationContext()
                .getFilesDir().getAbsolutePath());
        webViewSettings.setDatabaseEnabled(true);
        webViewSettings.setDatabasePath(getApplicationContext().getFilesDir()
                .getAbsolutePath() + "/databases");
        WebView test = new WebView(this); // getting default webview

        // user agent
        String userAgent = test.getSettings().getUserAgentString();
        webViewSettings.setUserAgentString(userAgent);

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
        webViewSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webViewSettings.setLoadsImagesAutomatically(true);
        webViewSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        return view;
    }

    public class myWebViewClient extends WebViewClient {


		@Override
        public void doUpdateVisitedHistory(WebView view, final String url,
                final boolean isReload) {

        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            progressBar.setVisibility(View.INVISIBLE);
            // view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            LogUtil.i(TAG, "页面加载完成");

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            currentUrl = url;

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
                    LayoutAlgorithm.NARROW_COLUMNS);
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            checkWebViewUrl(view, failingUrl);
        }

    }
    
   
}
