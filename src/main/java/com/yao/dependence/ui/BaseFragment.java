package com.yao.dependence.ui;

import android.content.Context;

import com.yao.dependence.net.APIService;
import com.yao.dependence.net.RequestManager;
import com.yao.devsdk.ui.SDKBaseFragment;

/**
 * fragment的基类
 */
public class BaseFragment extends SDKBaseFragment {

    //请求Manager
    protected RequestManager mRequestManager = RequestManager.getInstance();

    protected APIService mRequestService = mRequestManager.getRequestService();
    protected APIService mRequestServiceProxy = mRequestManager.getRequestServiceProxy();



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
