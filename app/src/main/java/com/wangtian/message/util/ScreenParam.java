package com.wangtian.message.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 获取屏幕参数
 *
 * @author rcm
 */
public class ScreenParam {

    private static final String TAG = ScreenParam.class.getSimpleName();
    public static int width;
    public static int height;
    public static int densityDpi;
    public static float density;

    private static Activity sActivity;
    private static ScreenParam sInstance;

    public synchronized static void init(Activity activity) {
        sActivity = activity;
        sInstance = new ScreenParam();
    }

    public static ScreenParam getInstance() {
        if (sInstance == null) {
            sInstance = new ScreenParam();
        }
        return sInstance;
    }

    private ScreenParam() {
        DisplayMetrics metric = new DisplayMetrics();
        if (sActivity != null) {
            sActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            width = metric.widthPixels;
            height = metric.heightPixels;
            density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
            densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
            Log.d(TAG, "屏幕分辨率:" + width + " * " + height);
            Log.d(TAG, "屏幕密度:" + density);
            Log.d(TAG, "屏幕密度DPI:" + densityDpi);
        }
    }
}
