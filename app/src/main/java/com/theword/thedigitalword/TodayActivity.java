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
    TextView bn = null;
    TextView greetingView = null;
    TextView bv = null;
    TextView bni = null;
    TextView bvi = null;
    ImageView shareText = null;
    TextView textRead = null;
    TextView imageRead = null;
    ImageView imageText = null;
    BitmapDrawable bitmapDrawable = null;
    Bitmap bitmap = null;

    String texti = "";
    String booki = "";
    String diri = "ltr";

    String dirH = "";
    String textBook = "";
    String textChapter = "";
    String imageBook = "";
    String imageChapter = "";

    String textHeading = "";
    String textString = "";
    String imageHeading = "";
    String imageString = "";

    String text = "";
    String book = "";
    String dir = "ltr";

    //For Social Media Tabs
    private SocialMediaAdapter adapter;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_baseline_twitter_24,
            R.drawable.ic_baseline_facebook_24
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this,SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        outsideView = (ImageView)findViewById(R.id.outside_image);
        imageView = (ImageView)findViewById(R.id.todayimage);
        bn = (TextView)findViewById(R.id.vodtb);
        bv = (TextView)findViewById(R.id.vodt);
        bni = (TextView)findViewById(R.id.vodtbi);
        bvi = (TextView)findViewById(R.id.vodti);
        shareText = (ImageView)findViewById(R.id.vodshare);
        textRead = (TextView) findViewById(R.id.vodtc);
        imageRead = (TextView) findViewById(R.id.vodtci);
        imageText = (ImageView)findViewById(R.id.vodsharei);

        dateView = (TextView)findViewById(R.id.todaydate);
        greetingView = (TextView)findViewById(R.id.todayGreeting);
        Context context = this.getApplicationContext();

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

        //Load Images in new Thread
        new Thread(){
            @Override
            public void run() {
                //getDrawable
                bitmapDrawable = new BitmapDrawable( getResources(),decodeSampledBitmapFromResource(getResources(),Util.getRandomSharedResource(context),350,250));
                bitmap = decodeSampledBitmapFromResource(getResources(),Util.getTodayImage(timeOfDay,context),150,220);
                //Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bvi.setBackground(bitmapDrawable);
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }

        }.start();

        //Run on Separate Thread
        new Thread(){
            @Override
            public void run() {
                // Get VOD
                JSONObject jObject = null;
                try {
                    String jsonData = TheWordContentService.getDailyVerse(sharedPreferences, context);
                    if(jsonData!=null && jsonData.trim().length()>0) {
                        jObject = new JSONObject(jsonData);
                        dir = jObject.getString("dir");
                        dirH = dir;
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
                        textHeading = book;
                        textString = text;
                      }
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bn.setText(book);
                        bn.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                        bv.setText(text);
                        bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);

                        //Read full chapter
                        textRead.setOnClickListener(v -> {
                            SharedPreferencesUtil.setBook(sharedPreferences,context, textBook);
                            SharedPreferencesUtil.setChapter(sharedPreferences,context,textChapter);
                            Intent i = new Intent(context,HomeActivity.class);
                            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        });

                        //Share content
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
                    }
                });
            }
        }.start();

        //Run on a separate Thread
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject jObjecti = null;
                    String jsonData = TheWordContentService.getRandomDailyVerse(sharedPreferences, context);
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
                    imageHeading = booki;
                    imageString=texti;
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bni.setText(booki);
                        bni.setTextDirection(diri.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                        bvi.setText(texti);
                        bvi.setTextDirection(diri.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);

                        //Read full Chapter
                        imageRead.setOnClickListener(v -> {
                            SharedPreferencesUtil.setBook(sharedPreferences,context, imageBook);
                            SharedPreferencesUtil.setChapter(sharedPreferences,context,imageChapter);
                            Intent i = new Intent(context,HomeActivity.class);
                            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        });
                        //Share Content
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
                    }
                });
            }
        }.start();

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
