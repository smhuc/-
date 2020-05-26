package com.wangtian.message.sociality;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wangtian.message.NewsListFragment;

import java.util.ArrayList;

/**
 * @Author Archy Wang
 * @Date 2019/3/26
 * @Description
 */

public class SocialVpAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> mTypes;

    public SocialVpAdapter(FragmentManager fm) {
        super(fm);
    }
    public SocialVpAdapter(FragmentManager fm, ArrayList<String> types) {
        super(fm);
        mTypes = types;
    }

    @Override
    public Fragment getItem(int position) {
        return  SocialListFragment.newInstance(mTypes.get(position));
    }

    @Override
    public int getCount() {
        return mTypes.size();
    }
}
