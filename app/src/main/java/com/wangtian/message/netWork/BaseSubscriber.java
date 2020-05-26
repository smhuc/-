package com.wangtian.message.netWork;

import android.util.Log;

import rx.Subscriber;
import rx.android.BuildConfig;

/**
 * @Author Archy Wang
 * @Date 2017/11/20
 * @Description
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {
    @Override
    public void onStart() {
        if (BuildConfig.DEBUG) Log.d("BaseSubscriber", "onStart");
    }

    @Override
    public void onCompleted() {
        if (BuildConfig.DEBUG) Log.d("BaseSubscriber", "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

}
