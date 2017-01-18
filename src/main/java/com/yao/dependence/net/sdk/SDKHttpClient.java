package com.yao.dependence.net.sdk;

import com.yao.devsdk.constants.SdkConst;
import com.yao.devsdk.net.response.HttpResult;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * Created by huichuan on 16/4/14.
 */
public class SDKHttpClient<T> {

    private static final String TAG = "HttpClient";

    public static String BASE_URL;

    //10秒链接超时
    private static final int CONNECT_TIMEOUT = 10;
    //20秒读超时
    private static final int READ_TIMEOUT = 100;

    Retrofit retrofit;

    T requestService;



    private static SDKHttpClient ourInstance;

    //获取单例
    public static <T> SDKHttpClient<T> getInstance(String baseUrl,Class<T> apiService){
        if (ourInstance == null){
            synchronized (HttpResult.class){
                if (ourInstance == null){
                    BASE_URL = baseUrl;
                    ourInstance = new SDKHttpClient<>(apiService,BASE_URL,CONNECT_TIMEOUT);
                }
            }
        }
        return ourInstance;
    }


    /**
     * 获取apiService
     * @return
     */
    public T getRequestService() {
        return requestService;
    }

    public SDKHttpClient(Class<T> apiService, String baseUrl, int connectTimeOut){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(connectTimeOut, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT,TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new SDKClientInterceptor());
        OkHttpClient okHttpClient = httpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        requestService = retrofit.create(apiService);
    }


    /**
     *
     * Created by huichuan on 16/4/14.
     */
    public class SDKClientInterceptor implements Interceptor {
        private static final String TAG = "SDKClientInterceptor";


        //拦截请求，增加公参
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            //执行请求
            Response response = chain.proceed(request);

            if (SdkConst.DEBUG) {
                //测试需要时打印返回数据格式
//                if (ClientInterceptor.isShowRawString) {
//                    String string = response.body().string();
//                    String unescapeJava = StringEscapeUtils.unescapeJava(string);
//                    LogUtil.i(TAG, "SDK response:" + unescapeJava);
//                }
            }


            return response;
        }




    }


}
