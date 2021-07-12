package com.theword.thedigitalword.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.theword.thedigitalword.FacebookFragment;
import com.theword.thedigitalword.TwitterFragment;

public class SocialMediaAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    public SocialMediaAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TwitterFragment tab1 = new TwitterFragment();
                return tab1;
            case 1:
                FacebookFragment tab2 = new FacebookFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
