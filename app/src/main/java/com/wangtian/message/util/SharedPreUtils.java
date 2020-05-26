package com.wangtian.message.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wangtian.message.MyApplication;


/**
 * @Author Archy Wang
 * @Date 2017/11/26
 * @Description
 */

public class SharedPreUtils {
    private static String mSpName = "UserInfo";
    private final static SharedPreferences mPref = MyApplication.getContext().getSharedPreferences(mSpName, Context.MODE_PRIVATE);
    private static SharedPreUtils mInstance;

    private SharedPreUtils() {
    }

    public static SharedPreUtils getInstance() {
        if (mInstance == null) {
            synchronized (SharedPreUtils.class) {
                if (mInstance == null) {
                    mInstance = new SharedPreUtils();
                }
            }
        }
        return mInstance;
    }

    public void setInt(String key, int value) {
        mPref.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }

    public void setLong(String key, long value) {
        mPref.edit().putLong(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        mPref.edit().putBoolean(key, value).apply();
    }

    public void setString(String key, String value) {
        mPref.edit().putString(key, value).apply();
    } public void setStringCommit(String key, String value) {
        mPref.edit().putString(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }
}
