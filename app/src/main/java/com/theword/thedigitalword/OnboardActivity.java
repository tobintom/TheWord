package com.theword.thedigitalword;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnboardActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    ImageButton mNextBtn;
    Button mSkipBtn, mFinishBtn;
    SharedPreferences sharedPreferences = null;

    ImageView zero, one, two;
    ImageView[] indicators;

    int lastLeftValue = 0;

    CoordinatorLayout mCoordinator;


    static final String TAG = "PagerActivity";

    int page = 0;   //  to track page position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this,SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_trans80));
        }

        setContentView(R.layout.activity_onboard);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mNextBtn = (ImageButton) findViewById(R.id.intro_btn_next);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            mNextBtn.setImageDrawable(
                    Util.tintMyDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_keyboard_arrow_right_24), Color.WHITE)
            );

        mSkipBtn = (Button) findViewById(R.id.intro_btn_skip);
        mFinishBtn = (Button) findViewById(R.id.intro_btn_finish);

        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.onboardLayout);


        indicators = new ImageView[]{zero, one, two};

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        final int color1 = ContextCompat.getColor(this, R.color.plum);
        final int color2 = ContextCompat.getColor(this, R.color.turq);
        final int color3 = ContextCompat.getColor(this, R.color.antq);

        final int[] colorList = new int[]{color1, color2, color3};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 2 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);

            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                updateIndicators(page);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        break;
                }


                mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem(page, true);
            }
        });

        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnboardActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  update 1st time pref
                SharedPreferencesUtil.saveIntroSeen(sharedPreferences,OnboardActivity.this,"true");
                Intent i = new Intent(OnboardActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
             return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        ImageView img;

        int[] bgs = new int[]{R.drawable.bible, R.drawable.ic_baseline_book_24, R.drawable.ic_baseline_read_more_24};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_onboard, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView textTitle = (TextView) rootView.findViewById(R.id.section_Title);
            TextView textDescView = (TextView) rootView.findViewById(R.id.section_desc);
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            textTitle.setText("The Digital Word");
            img = (ImageView) rootView.findViewById(R.id.section_img);
            img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);
            String desc = "";
            switch (section){
                case 1:
                    textView.setText("Welcome!");
                    desc = "Read the Word of God in over 60 languages, complete with <b>Cross References</b>, <b>Search Features</b>, <b>Personalization</b> and lots more." + System.getProperty("line.separator") +
                            "Click on the <b>right arrow</b> button below and click on <b>Finish</b> to complete the Welcome Introduction.";
                    SpannableStringBuilder sb = new SpannableStringBuilder(desc);
                    Pattern p = Pattern.compile("<b>.*?</b>", Pattern.CASE_INSENSITIVE);
                    boolean stop = false;
                    while (!stop)
                    {
                        Matcher m = p.matcher(sb.toString());
                        if (m.find()) {
                            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            sb.delete(m.end()-4, m.end());
                            sb.delete(m.start(), m.start() + 3);
                        }
                        else
                            stop = true;
                    }
                    textDescView.setText(sb);
                    break;
                case 2:
                    textView.setText("Bible Text");
                    desc = "<b>Press and Hold</b> on a Bible verse to see it's <b>Cross References</b>." + System.getProperty("line.separator") +
                            "<b>Single click</b> on one or multiple verses to Personalize with Notes, Bookmarks, Highlights or Copy and Share!";
                    SpannableStringBuilder sb1 = new SpannableStringBuilder(desc);
                    Pattern p1 = Pattern.compile("<b>.*?</b>", Pattern.CASE_INSENSITIVE);
                    boolean stop1 = false;
                    while (!stop1)
                    {
                        Matcher m = p1.matcher(sb1.toString());
                        if (m.find()) {
                            sb1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            sb1.delete(m.end()-4, m.end());
                            sb1.delete(m.start(), m.start() + 3);
                        }
                        else
                            stop1 = true;
                    }
                    textDescView.setText(sb1);

                    break;
                case 3:
                    textView.setText("More");
                    desc = "Enjoy lots of other features including searching the Bible with passages or text, setting Home Screen Widgets containing Daily Verses and much more..";
                    textDescView.setText(desc);
                    break;
            }




            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }


}