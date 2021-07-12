package com.theword.thedigitalword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.theword.thedigitalword.adapter.SocialMediaAdapter;
import com.theword.thedigitalword.adapter.TabAdapter;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

public class TodayActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    BottomNavigationView navigationView = null;
    ImageView imageView = null;
    ImageView outsideView = null;
    TextView dateView = null;
    TextView greetingView = null;
    String dirH = "";
    String textBook = "";
    String textChapter = "";
    String imageBook = "";
    String imageChapter = "";

    String textHeading = "";
    String textString = "";
    String imageHeading = "";
    String imageString = "";

    //For Social Media Tabs
    private SocialMediaAdapter adapter;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_baseline_twitter_24,
            R.drawable.ic_baseline_facebook_24
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_today);

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        outsideView = (ImageView)findViewById(R.id.outside_image);
        imageView = (ImageView)findViewById(R.id.todayimage);

        dateView = (TextView)findViewById(R.id.todaydate);
        greetingView = (TextView)findViewById(R.id.todayGreeting);
        // Get VOD
        JSONObject jObject = null;
        String text = "";
        String book = "";
        String dir = "ltr";
        try {
            String jsonData = TheWordContentService.getDailyVerse(sharedPreferences, this.getApplicationContext());
            if(jsonData!=null && jsonData.trim().length()>0) {
                jObject = new JSONObject(jsonData);
                dir = jObject.getString("dir");
                this.dirH = dir;
                JSONObject o = (JSONObject) jObject.getJSONArray("passages").get(0);
                book = o.getString("name");
                textBook = o.getString("book");
                //get rest
                JSONObject p = (JSONObject)o.getJSONArray("content").get(0);
                textChapter = p.getString("chapter");
                book = book +" "+ p.getString("chapter");
                JSONArray textArray = (JSONArray) p.getJSONArray("verses");
                for(int i=0;i<textArray.length();i++){
                    JSONObject ol = (JSONObject) textArray.get(i);
                    text = text + ol.getString("verse") +".  " +ol.getString("text") + System.getProperty("line.separator");
                }

                TextView bn = (TextView)findViewById(R.id.vodtb);
                bn.setText(book);
                textHeading = book;
                bn.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                TextView bv = (TextView)findViewById(R.id.vodt);
                bv.setText(text);
                textString = text;
                bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONObject jObjecti = null;
            String texti = "";
            String booki = "";
            String diri = "ltr";
            String jsonData = TheWordContentService.getRandomDailyVerse(sharedPreferences, this.getApplicationContext());
            if(jsonData!=null && jsonData.trim().length()>0) {
                jObjecti = new JSONObject(jsonData);
                diri = jObjecti.getString("dir");
                JSONObject o = (JSONObject) jObjecti.getJSONArray("passages").get(0);
                booki = o.getString("name");
                imageBook = o.getString("book");
                //get rest
                JSONObject p = (JSONObject)o.getJSONArray("content").get(0);
                imageChapter = p.getString("chapter");
                booki = booki +" "+ p.getString("chapter");
                JSONObject t = (JSONObject) p.getJSONArray("verses").get(0);
                booki = booki+":"+t.getString("verse");
                texti = t.getString("text")+ "\n";
            }
            TextView bni = (TextView)findViewById(R.id.vodtbi);
            bni.setText(booki);
            imageHeading = booki;
            bni.setTextDirection(diri.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
            TextView bvi = (TextView)findViewById(R.id.vodti);
            bvi.setBackground(new BitmapDrawable( getResources(),decodeSampledBitmapFromResource(getResources(),Util.getRandomSharedResource(this.getApplicationContext()),350,250)));
            bvi.setText(texti);
            imageString=texti;
            bvi.setTextDirection(diri.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Read full chapter
        TextView textRead = (TextView) findViewById(R.id.vodtc);
        textRead.setOnClickListener(v -> {
            SharedPreferencesUtil.setBook(sharedPreferences,this, textBook);
            SharedPreferencesUtil.setChapter(sharedPreferences,this,textChapter);
            Intent i = new Intent(this,HomeActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        TextView imageRead = (TextView) findViewById(R.id.vodtci);
        imageRead.setOnClickListener(v -> {
            SharedPreferencesUtil.setBook(sharedPreferences,this, imageBook);
            SharedPreferencesUtil.setChapter(sharedPreferences,this,imageChapter);
            Intent i = new Intent(this,HomeActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        //Share content
        ImageView shareText = (ImageView)findViewById(R.id.vodshare);
        shareText.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/*");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Digital Word Verse");
            String message = "<h1><b><span dir ='"+ dirH +"' >"+ textHeading+"</span></b></h1>" +
                                "<p dir='"+ dirH+"'> " + textString +"</p><BR>" +
                                "<a href='https://thedigitalword.org'>https://thedigitalword.org</a>";
            shareIntent.putExtra(Intent.EXTRA_TEXT, HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
            startActivity(Intent.createChooser(shareIntent, "Share with:"));
        });

        ImageView imageText = (ImageView)findViewById(R.id.vodsharei);
        imageText.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/*");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Digital Word Verse");
            String message = "<h1><b><span dir ='"+ dirH +"' >"+ imageHeading+"</span></b></h1>" +
                    "<p dir='"+ dirH+"'> " + imageString +"</p><BR>" +
                    "<a href='https://thedigitalword.org'>https://thedigitalword.org</a>";
            shareIntent.putExtra(Intent.EXTRA_TEXT, HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
            startActivity(Intent.createChooser(shareIntent, "Share with:"));
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_read:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        break;
                    case R.id.action_me:
                        startActivity(new Intent(getApplicationContext(), MeActivity.class));
                        break;
                    case R.id.action_search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        break;
                    case R.id.action_more:
                        startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                        break;
                    default:

                        break;
                }
                return true;
            }
        });
        navigationView.setSelectedItemId(R.id.action_today);
        //Set the Date Greeting and Image
        Formatter fmt = new Formatter();
        Calendar cal = Calendar.getInstance();
        fmt.format("%tA, %tB %td", cal, cal, cal);
        dateView.setText(fmt.toString());
        int timeOfDay = cal.get(Calendar.HOUR_OF_DAY);
        String greeting = "";

        if(timeOfDay >= 0 && timeOfDay < 12){
            greeting =  "Good Morning";
            outsideView.setBackgroundColor(Color.rgb(229, 220, 210));
        }else if(timeOfDay >= 12 && timeOfDay < 17){
            greeting = "Good Afternoon";
            outsideView.setBackgroundColor(Color.rgb(255, 240, 189));
        }else if(timeOfDay >= 17 && timeOfDay < 20){
            greeting = "Good Evening";
            outsideView.setBackgroundColor(Color.rgb(229, 164, 198));
        }else if(timeOfDay >= 20 && timeOfDay < 24){
            greeting =  "Good Evening";
            outsideView.setBackgroundColor(Color.rgb(188, 184, 206));
        }
        greetingView.setText(greeting);
        imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(),Util.getTodayImage(timeOfDay,this),150,220));

        //Set TABS
        tabLayout = (TabLayout) findViewById(R.id.tabsocial);
        tabLayout.addTab(tabLayout.newTab().setText("Twitter").setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setText("Facebook").setIcon(tabIcons[1]));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.socialview_pager);
        final SocialMediaAdapter adapter = new SocialMediaAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
        finishAffinity();
    }

    public static  int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
