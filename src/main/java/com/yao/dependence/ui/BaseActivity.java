package com.yao.dependence.ui;

import android.net.Uri;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.yao.dependence.net.APIService;
import com.yao.dependence.net.RequestManager;
import com.yao.devsdk.ui.SDKBaseActivity;


public class BaseActivity extends SDKBaseActivity{

    //@Bind fields must not be private or static  这都咋在编译的时候就能报错呢？

    //网络请求Manager
    protected RequestManager mRequestManager = RequestManager.getInstance();
    //网络请求的Service,用于配合RxJava发起一组请求
    protected APIService mAPIServiceRaw = mRequestManager.getRequestService();
    protected APIService mRequestServiceProxy = mRequestManager.getRequestServiceProxy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
