package com.yao.dependence.net;

import com.yao.devsdk.log.LoggerUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 一个空的proxy，目前只是练习作用
 */
public class ApiServiceProxyHandler implements InvocationHandler {
    private static final String TAG = "ApiServiceProxyHandler";
    private Object rawObj;

    public ApiServiceProxyHandler(Object rawObj) {
        this.rawObj = rawObj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在转调具体目标对象之前，可以执行一些功能处理

        LoggerUtil.i(TAG,"before method invoke");

        //转调具体目标对象的方法
        Object invoke = method.invoke(rawObj, args);
        //在转调具体目标对象之后，可以执行一些功能处理
        LoggerUtil.i(TAG,"after method invoke");

        if (invoke instanceof Observable){
            Observable observable = (Observable) invoke;
            invoke = observable.subscribeOn(Schedulers.io());
        }

        return invoke;

    }
} 