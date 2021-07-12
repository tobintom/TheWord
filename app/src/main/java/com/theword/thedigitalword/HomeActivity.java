package com.theword.thedigitalword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theword.thedigitalword.component.BaseActivity;
import com.theword.thedigitalword.listener.SwipeListener;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.MyLeadingMarginSpan2;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.Year;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    JSONObject jObject = null;
    String text = "";
    String dir = "ltr";
    String name= "";
    String engName = "";
    String lang = "";
    String displayBook;
    String engBook;
    String toolBook;
    JSONArray textArray = null;
    String textString = "";
    TextView bv = null;
    TextView bn = null;
    TextView be = null;
    ImageView iv = null;
    ImageView iv2 = null;
    SharedPreferences sharedPreferences = null;
    TextView tb = null;
    TextView tl = null;
    int ORIENTATION;
    FloatingActionButton fabRight = null;
    FloatingActionButton fabLeft = null;
    BottomNavigationView navigationView = null;
    TextView bookBottom = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        ORIENTATION = this.getResources().getConfiguration().orientation;

        String book = SharedPreferencesUtil.getBook(sharedPreferences,this.getApplicationContext());
        String chapter = SharedPreferencesUtil.getChapter(sharedPreferences,this.getApplicationContext());
        try {
            String jsonData = TheWordContentService.getChapter(sharedPreferences,this.getApplicationContext(),book,chapter);
            if(jsonData!=null && jsonData.trim().length()>0) {
                jObject = new JSONObject(jsonData);
                dir = jObject.getString("dir");
                name = jObject.getString("name");
                engName = jObject.getString("english");
                textArray =  jObject.getJSONArray("verses");
                lang = jObject.getString("id");
                chapter = jObject.getString("chapter");
                if(name!=null && name.length()>20 && ORIENTATION == Configuration.ORIENTATION_PORTRAIT){
                   toolBook= name.substring(0,12) + "..."+name.substring(name.length()-3) +" "+chapter;
                }else{
                    toolBook =  name +" "+chapter;
                }

                displayBook = name +" "+chapter;
                engBook = engName +" "+chapter;
                for(int i=0;i<textArray.length();i++){
                    JSONObject o = (JSONObject) textArray.get(i);
                    textString = textString+ o.getString("verse") +" " +o.getString("text") + System.getProperty("line.separator");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_home);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fabLeft = (FloatingActionButton)findViewById(R.id.fabLeft);
        fabRight = (FloatingActionButton)findViewById(R.id.fabRight);
        bv =  (TextView) findViewById(R.id.home_verse_text);
        bn =  (TextView) findViewById(R.id.home_book);
        be = (TextView)findViewById(R.id.bookenglish);
        iv = (ImageView) findViewById(R.id.bookicon);
        iv.setImageResource(Util.getBibleBookImage(book,this.getApplicationContext()));
        iv2 = (ImageView)findViewById(R.id.outside_image);
        iv.setBackgroundColor(Util.getBookColor(book));
        iv2.setBackgroundColor(Util.getBookOutsideColor(book));

        tb = (TextView) findViewById(R.id.toolbook);
        tl = (TextView)findViewById(R.id.toollang) ;
        bookBottom = (TextView)findViewById(R.id.bookBottom);

        tb.setText(toolBook);
        tl.setText(ApplicationData.getLanguage(lang));
        bookBottom.setText(displayBook);

        if(!SharedPreferencesUtil.getLanguage(sharedPreferences,this.getApplicationContext()).equalsIgnoreCase("ENG")){
            be.setText(engBook);
        }

        bn.setText(displayBook);
        bn.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
        bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
        bookBottom.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
        bv.setMovementMethod(LinkMovementMethod.getInstance());
        bv.setText(addClickablePart(textString), TextView.BufferType.SPANNABLE);

        fabRight.setOnClickListener(v -> {
            nextChapter();
        });

        fabLeft.setOnClickListener(v -> {
            previousChapter();
        });

        //Set Swipe Listener
        bv.setOnTouchListener(new SwipeListener(HomeActivity.this){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                nextChapter();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                previousChapter();

            }
        });

        tl.setOnClickListener(v ->
        {
            Intent i = new Intent(this,LanguageSelectorActivity.class);
            startActivity(i);

        } );

        tb.setOnClickListener(v -> {
            Intent i = new Intent(this, BookSelector.class);
            startActivity(i);
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_today:
                        startActivity(new Intent(getApplicationContext(), TodayActivity.class));
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

        navigationView.setSelectedItemId(R.id.action_read);
        TextView v = (TextView) findViewById(R.id.home_footer1);
        v.setText("\u00a9 "+Calendar.getInstance().get(Calendar.YEAR)+" The Digital Word");
    }



    private void previousChapter(){
        try{
            String book = SharedPreferencesUtil.getBook(sharedPreferences,this.getApplicationContext());
            String chapter = SharedPreferencesUtil.getChapter(sharedPreferences,this.getApplicationContext());
            String jsonData = TheWordContentService.getPreviousChapter(sharedPreferences,this.getApplicationContext(),book,chapter);
            String newBook = "";
            String newChapter = "";
            if(jsonData!=null && jsonData.trim().length()>0) {
                textString = "";
                jObject = new JSONObject(jsonData);
                dir = jObject.getString("dir");
                name = jObject.getString("name");
                engName = jObject.getString("english");
                textArray =  jObject.getJSONArray("verses");
                newBook = jObject.getString("number");
                newChapter = jObject.getString("chapter");
                lang = jObject.getString("id");
                if(name!=null && name.length()>20 && ORIENTATION== Configuration.ORIENTATION_PORTRAIT){
                    toolBook= name.substring(0,12) + "..."+name.substring(name.length()-3) +" "+newChapter;
                }else{
                    toolBook =  name +" "+newChapter;
                }
                displayBook = name +" "+newChapter;
                engBook = engName +" "+newChapter;
                //Set the new Book and Chapter
                SharedPreferencesUtil.setBook(sharedPreferences,this.getApplicationContext(),newBook);
                SharedPreferencesUtil.setChapter(sharedPreferences,this.getApplicationContext(),newChapter);
                for(int i=0;i<textArray.length();i++){
                    JSONObject o = (JSONObject) textArray.get(i);
                    textString = textString+ o.getString("verse") +" " +o.getString("text") + System.getProperty("line.separator");
                }
                //refresh
                iv.setImageResource(Util.getBibleBookImage(newBook,this.getApplicationContext()));
                iv.setBackgroundColor(Util.getBookColor(newBook));
                iv2.setBackgroundColor(Util.getBookOutsideColor(newBook));
                bn.setText(displayBook);
                if(!SharedPreferencesUtil.getLanguage(sharedPreferences,this.getApplicationContext()).equalsIgnoreCase("ENG")){
                    be.setText(engBook);
                }
                bookBottom.setText(displayBook);
                tb.setText(toolBook);
                tl.setText(ApplicationData.getLanguage(lang));
                bn.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                bookBottom.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                bv.setMovementMethod(LinkMovementMethod.getInstance());
                bv.setText(addClickablePart(textString), TextView.BufferType.SPANNABLE);

            }

        }catch(Exception e){

        }

    }

    private void nextChapter(){
        try{
            String book = SharedPreferencesUtil.getBook(sharedPreferences,this.getApplicationContext());
            String chapter = SharedPreferencesUtil.getChapter(sharedPreferences,this.getApplicationContext());
            String jsonData = TheWordContentService.getNextChapter(sharedPreferences,this.getApplicationContext(),book,chapter);
            String newBook = "";
            String newChapter = "";
            if(jsonData!=null && jsonData.trim().length()>0) {
                textString = "";
                jObject = new JSONObject(jsonData);
                dir = jObject.getString("dir");
                name = jObject.getString("name");
                engName = jObject.getString("english");
                textArray =  jObject.getJSONArray("verses");
                newBook = jObject.getString("number");
                newChapter = jObject.getString("chapter");
                lang = jObject.getString("id");
                if(name!=null && name.length()>20 && ORIENTATION== Configuration.ORIENTATION_PORTRAIT){
                    toolBook= name.substring(0,12) + "..."+name.substring(name.length()-3) +" "+newChapter;
                }else{
                    toolBook =  name +" "+newChapter;
                }
                displayBook = name +" "+newChapter;
                engBook = engName + " "+newChapter;
                //Set the new Book and Chapter
                SharedPreferencesUtil.setBook(sharedPreferences,this.getApplicationContext(),newBook);
                SharedPreferencesUtil.setChapter(sharedPreferences,this.getApplicationContext(),newChapter);
                for(int i=0;i<textArray.length();i++){
                    JSONObject o = (JSONObject) textArray.get(i);
                    textString = textString+ o.getString("verse") +" " +o.getString("text") + System.getProperty("line.separator");
                }
                //refresh
                iv.setImageResource(Util.getBibleBookImage(newBook,this.getApplicationContext()));
                iv.setBackgroundColor(Util.getBookColor(newBook));
                iv2.setBackgroundColor(Util.getBookOutsideColor(newBook));
                if(!SharedPreferencesUtil.getLanguage(sharedPreferences,this.getApplicationContext()).equalsIgnoreCase("ENG")){
                    be.setText(engBook);
                }
                bn.setText(displayBook);
                bookBottom.setText(displayBook);
                tb.setText(toolBook);
                tl.setText(ApplicationData.getLanguage(lang));
                bn.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                bookBottom.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                bv.setMovementMethod(LinkMovementMethod.getInstance());
                bv.setText(addClickablePart(textString), TextView.BufferType.SPANNABLE);

            }

        }catch(Exception e){

        }

    }

    private SpannableStringBuilder addClickablePart(String str) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        if(str!=null && str.trim().length()>0) {
            String[] strings = str.split(System.getProperty("line.separator"));

            for (String stringPart : strings) {
                int start = str.indexOf(stringPart);
                int end = stringPart.length() + start;
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, start + stringPart.split(" ")[0].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(0.8f), start,start + stringPart.split(" ")[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLACK);
                        ds.setUnderlineText(false);
                    }

                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(HomeActivity.this, stringPart,
                                Toast.LENGTH_SHORT).show();
                    }

                }, start, end, 0);

            }
        }
        return ssb;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
