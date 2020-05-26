package com.wangtian.message.netWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Author Archy Wang
 * @Date 2017/12/15
 * @Description
 */

public class LiveNetWorkMonitor implements NetWorkMonitor {

    private final Context mApplicationContext;

    public LiveNetWorkMonitor(Context context) {
        mApplicationContext = context.getApplicationContext();
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        return activeNetwork!=null&&activeNetwork.isAvailable();
    }
}
