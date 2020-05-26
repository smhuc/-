package com.wangtian.message.netWork;

import android.util.Log;
import android.widget.Toast;

import com.wangtian.message.MyApplication;


/**
 * @Author Archy Wang
 * @Date 2017/12/14
 * @Description
 */

public abstract class NetWorkSubscriber<T> extends BaseSubscriber<T> {
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof LoginTimeOutException) {
            if (Thread.currentThread().getName().equals("main")) {
                Toast.makeText(MyApplication.getContext(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            }
        } else if (e instanceof NoNetworkException) {
            if (Thread.currentThread().getName().equals("main")) {
                Toast.makeText(MyApplication.getContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (Thread.currentThread().getName().equals("main")) {
                Toast.makeText(MyApplication.getContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
            Log.e("NetWorkSubscriber", "onError: " + e.getMessage());
        }

    }
}
