package com.wangtian.message;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * @Author Archy Wang
 * @Date 2019/3/26
 * @Description
 */

public class NewsVpAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> mTypes;

    public NewsVpAdapter(FragmentManager fm) {
        super(fm);
    }

    public NewsVpAdapter(FragmentManager fm, ArrayList<String> types) {
        super(fm);
        mTypes = types;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsListFragment.newInstance(mTypes.get(position));
    }

    @Override
    public int getCount() {
        return mTypes.size();
    }
}
