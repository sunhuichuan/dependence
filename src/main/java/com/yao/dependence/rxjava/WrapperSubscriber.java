package com.yao.dependence.rxjava;

import com.yao.devsdk.log.LoggerUtil;

import rx.Subscriber;

/**
 * 普通请求的包装
 * Created by huichuan on 16/8/7.
 */
public abstract class WrapperSubscriber<T> extends Subscriber<T> {
    private static final String TAG = "WrapperSubscriber";



    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        LoggerUtil.e(TAG,"请求异常了Exception:-->",e);
        onWrapperError(e);
    }

    @Override
    public void onNext(T t) {
        LoggerUtil.i(TAG,"请求到的结果：-->"+t.toString());
        onWrapperNext(t);
    }

    public abstract void onWrapperNext(T t);

    public abstract void onWrapperError(Throwable e);

}
