package com.theword.thedigitalword;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.theword.thedigitalword.adapter.TabAdapter;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookSelector extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_baseline_book_24,
            R.drawable.ic_baseline_list_24
    };

    private @ColorInt  int selectedColor;
    private @ColorInt  int nonSelectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_selector);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(R.attr.textAppearanceHeadline1, typedValue, true);
        nonSelectedColor = typedValue.data;

        TypedValue typedValue1 = new TypedValue();
        Resources.Theme theme1 = this.getTheme();
        theme1.resolveAttribute(R.attr.titleTextColor, typedValue1, true);
        selectedColor = typedValue1.data;

        Toolbar toolbar = (Toolbar) findViewById(R.id.book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tv = (TextView)findViewById(R.id.bookselect);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.setSelectedBook(null);
                finish();
            }
        });
        //define tabs
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        adapter = new TabAdapter(getSupportFragmentManager(),this.getApplicationContext());
        adapter.addFragment(new FirstFragment(), "Books",tabIcons[0]);
        adapter.addFragment(new First2Fragment(), "Chapters",tabIcons[1]);

        //PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        //viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                highLightCurrentTab(tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
              //  FadeTab(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.setSelectedBook(null);
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i,nonSelectedColor));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position,selectedColor));
        if(selectedColor == Color.parseColor("#FBF9F9")) {
            tabLayout.setSelectedTabIndicatorColor(selectedColor);
        }
    }
}