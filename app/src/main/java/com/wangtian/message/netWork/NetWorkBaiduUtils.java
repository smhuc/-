package com.wangtian.message.netWork;


import com.wangtian.message.BuildConfig;
import com.wangtian.message.MyApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author Archy Wang
 * @Date 2017/11/20
 * @Description
 */

public class NetWorkBaiduUtils {
    private final Retrofit mRetrofit;
    private static class NetWorkHolder {
        private static final NetWorkBaiduUtils sInstance = new NetWorkBaiduUtils();
    }
    private NetWorkBaiduUtils() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        LiveNetWorkMonitor liveNetworkMonitor = new LiveNetWorkMonitor(MyApplication.getContext());
        httpClientBuilder.connectTimeout(TimeConstant.TIMEOUT_CONNECTION, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(TimeConstant.TIMEOUT_READ, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(TimeConstant.TIMEOUT_WRITE, TimeUnit.SECONDS);


//        httpClientBuilder.addInterceptor(new JsonInterceptor());
        httpClientBuilder.addInterceptor(chain -> {
            boolean connected = liveNetworkMonitor.isConnected();
            if (liveNetworkMonitor.isConnected()) {
                return chain.proceed(chain.request());
            } else {
                throw new NoNetworkException();
            }
        });

        mRetrofit = new Retrofit.Builder()
                .baseUrl(NetConstant.BAIDU_URL_RELEASE_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();


    }


    public Retrofit getRetrofit(){
        return mRetrofit;
    }

    /**
     * 返回统一的Service
     * @return ICleanService
     */
    public IBaiduNetInterface getInterfaceService(){
        return mRetrofit.create(IBaiduNetInterface.class);
    }



    public static NetWorkBaiduUtils getInstance(){
        return NetWorkHolder.sInstance;
    }


}
