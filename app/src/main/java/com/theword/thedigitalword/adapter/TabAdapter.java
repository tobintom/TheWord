package com.theword.thedigitalword.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<Integer> mFragmentIconList = new ArrayList<>();
    private Context context;

    public TabAdapter(FragmentManager fm){
        super(fm);
    }
    public TabAdapter(FragmentManager fm, Context context1) {
        super(fm);
        this.context = context1;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    public void addFragment(Fragment fragment, String title, int icon) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        mFragmentIconList.add(icon);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public View getTabView(int position, int color) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        ImageView iv = view.findViewById(R.id.tabImageView);
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setTextSize(18);



        iv.setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
        tabTextView.setTextColor(color);
        tabTextView.setText(mFragmentTitleList.get(position));
        return view;
    }

    public View getSelectedTabView(int position, int color) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);

        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(mFragmentTitleList.get(position));
        tabTextView.setTextSize(18); // for big text, increase text size

        tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(mFragmentIconList.get(position));



        tabTextView.setTextColor(color);
        tabImageView.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        return view;
    }

}