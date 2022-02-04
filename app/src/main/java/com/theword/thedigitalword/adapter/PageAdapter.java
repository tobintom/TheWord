package com.theword.thedigitalword.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.theword.thedigitalword.First2Fragment;
import com.theword.thedigitalword.FirstFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    public PageAdapter(FragmentManager fm, int numberTabs){
        super(fm);
        this.numberOfTabs = numberTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               return new FirstFragment();
           case 1:
               return new First2Fragment();
           default:
               return null;
       }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
