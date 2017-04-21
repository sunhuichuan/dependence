package com.yao.dependence.net;

import android.os.Handler;
import android.os.Looper;

import com.yao.devsdk.log.LoggerUtil;
import com.yao.devsdk.net.exception.ApiException;
import com.yao.devsdk.net.response.HttpResult;

import rx.Subscriber;

/**
 * 网络请求的包装
 * Created by huichuan on 16/8/7.
 */
public abstract class NetSubscriber<T> extends Subscriber<HttpResult<T>> {
    private static final String TAG = "NetSubscriber";


    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());


    @Override
    public void onStart() {

        LoggerUtil.i(TAG,"onStart--Thread: "+Thread.currentThread().getId());
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                LoggerUtil.i(TAG,"onStart--mainThread: "+Thread.currentThread().getId());

            }
        });
    }

    @Override
    public void onError(final Throwable e) {
        LoggerUtil.e(TAG,"请求异常了Exception:-->",e);
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                //在主线程处理Observer

                onWrapperError(e);
                onWrapperEnd();

            }
        });
    }

    @Override
    public void onNext(final HttpResult<T> httpResult) {
        LoggerUtil.i(TAG,"请求到的结果：-->"+httpResult.toString());
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                //在主线程处理Observer

                if (httpResult.getStatus() != ApiException.OK) {
                    Exception exception = new ApiException(httpResult.getStatus(),httpResult.getMessage());
                    onWrapperError(exception);
                }else{
                    //请求成功
                    T t = httpResult.getData();
                    onWrapperNext(t);

                }

            }
        });

    }


    @Override
    public void onCompleted() {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                onWrapperEnd();
            }
        });
    }

    /**
     * {@link #onStart()} 方法的主线程替代者
     */
    public void onWrapperStart(){}

    public abstract void onWrapperNext(T t);

    public abstract void onWrapperError(Throwable e);
    /**
     * {@link #onCompleted()} 或者 {@link #onError(Throwable)} 方法被执行后，执行此方法
     */
    public void onWrapperEnd(){}

}
