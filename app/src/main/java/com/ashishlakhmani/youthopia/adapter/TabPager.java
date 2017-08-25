package com.ashishlakhmani.youthopia.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ashishlakhmani.youthopia.fragment.HomeFragment;
import com.ashishlakhmani.youthopia.fragment.RegisteredFragment;

public class TabPager extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public TabPager(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new RegisteredFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }


}
