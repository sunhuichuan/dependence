package com.yao.dependence.ui;

import android.content.Intent;
import android.os.Bundle;

import com.yao.devsdk.permission.PermissionManager;

import butterknife.ButterKnife;

/**
 *
 * 封装了基本的ButterKnife的Activity
 * Created by huichuan on 16/8/14.
 */
public abstract class BaseButterActivity extends BaseActivity {

    protected PermissionManager mPermissionManager = PermissionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentViewLayoutId());
        ButterKnife.bind(thisContext, this);

        Intent intent = getIntent();
        if (intent!=null){
            initIntent(intent);
        }
        initTitleBar();
        initViews();
        initData();

    }

    /**
     * containerViewLayoutId
     */
    protected abstract int contentViewLayoutId();

    /**
     * 初始化Intent包含的信息
     *
     * <p>
     *     非必须实现，因为部分Activity不需要数据传递
     *     例如：搜索、登录、注册页面
     * </p>
     */
    protected void initIntent(Intent intent){}
    /**
     * 初始化TitleBar
     */
    protected void initTitleBar(){}
    /**
     * 初始化View
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     * <p>
     *     非必须实现，因为部分Activity在Fragment中加载数据
     * </p>
     */
    protected void initData(){}



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(thisContext);
    }

    /**
     * 权限授权结果回调
     * @param requestCode 个人认为此requestCode是系统为了【业务调用方区分同一个权限在不同地方发起请求】增加的参数，一般情况可以不使用
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
            String permissions[], int[] grantResults) {

        mPermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);


    }


}
