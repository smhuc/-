package com.wangtian.message;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.wangtian.message.bean.User;
import com.wangtian.message.netBean.LoginBean;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends MultiDexApplication {

    private static Context ctx;
    private List<Activity> activityList = new LinkedList<Activity>();
    private static MyApplication instance;
    public static boolean isshow = true;
    public static User user = new User();
    public static LoginBean sLoginBean;
    public static int sina = 0;

    public static String newsKeyword = "";
    public static String socialKeyword = "";

    @Override
    public void onCreate() {
        super.onCreate();

        ctx = this;
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }*/
    // 单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;

    }

    public static Context getContext() {
        return ctx;
    }


    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    /*
     * 在每一个Activity中的onCreate方法里添加该Activity到MyApplication对象实例容器中
     *
     * MyApplication.getInstance().addActivity(this);
     *
     * 在需要结束所有Activity的时候调用exit方法
     *
     * MyApplication.getInstance().exit();
     */
    public void exit() {

        for (Activity activity : activityList) {
            activity.finish();
        }

        // System.exit(0);

    }
}
