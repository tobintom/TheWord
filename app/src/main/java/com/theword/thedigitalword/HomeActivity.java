package com.theword.thedigitalword;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theword.thedigitalword.component.AddBibleNoteDialog;
import com.theword.thedigitalword.component.AddBookmarkDialog;
import com.theword.thedigitalword.component.ColoredUnderlineSpan;
import com.theword.thedigitalword.component.CrossRefDialog;
import com.theword.thedigitalword.component.LongClickMovementMethod;
import com.theword.thedigitalword.component.LongClickableSpan;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.listener.SwipeListener;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.service.TheWordMetaService;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class HomeActivity extends AppCompatActivity {
    FragmentManager fm = null;
    JSONObject jObject = null;
    boolean dark = false;
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
    Context context = null;
    String book = "";
    String chapter = "";

    TextView textHelp = null;

    LinearLayout hintOne = null;
    LinearLayout hintTwo = null;
    LinearLayout hintThree = null;
    RelativeLayout topLevelLayout = null;
    int i=0;

    //Spannable Images
    Drawable dFav = null;
    Drawable dNote = null;
    Drawable dBook = null;

    ImageView hdelete = null;

    //BottomSheet
    LinearLayout layoutBottom = null;
    BottomSheetBehavior sheetBehavior = null;
    ImageView bsclose = null;

    //Keep tab of selected text
    LinearLayout colorLayout = null;
    Map<Integer,String> selectedText =  new TreeMap<Integer, String>();
    Map<String, BibleDBContent> highlightVerses = null;
    Map<String, BibleDBContent> favoriteVerses = null;
    Map<String, BibleDBContent> bookmarkVerses = null;
    Map<String, BibleDBContent> notesVerses = null;

    BibleHelper _helper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences, this));
        super.onCreate(savedInstanceState);
        ORIENTATION = this.getResources().getConfiguration().orientation;
        context = this.getApplicationContext();
        fm = getSupportFragmentManager();
        setContentView(R.layout.activity_home);
        _helper = new BibleHelper(context);
        if (Util.checkConnection(context)) {

            topLevelLayout = findViewById(R.id.top_layout);
            fabLeft = (FloatingActionButton) findViewById(R.id.fabLeft);
            fabRight = (FloatingActionButton) findViewById(R.id.fabRight);

            //Check for First time
            boolean ranBefore = sharedPreferences.getBoolean("RanBefore", false);
            if(!ranBefore){
                //Show the Demo
                hintOne = findViewById(R.id.hintOne);
                hintTwo = findViewById(R.id.hintTwo);
                hintThree = findViewById(R.id.hintThree);
                hintOne.setVisibility(View.VISIBLE);
                hintTwo.setVisibility(View.GONE);
                hintThree.setVisibility(View.GONE);

                topLevelLayout.setVisibility(View.VISIBLE);
                textHelp = findViewById(R.id.textHelp);
                textHelp.setText("Next");

                textHelp.setOnClickListener(v -> {
                    i++;
                    if(i==1){
                        topLevelLayout.setVisibility(View.VISIBLE);
                        hintOne.setVisibility(View.GONE);
                        hintTwo.setVisibility(View.VISIBLE);
                        hintThree.setVisibility(View.GONE);
                    }
                    if(i==2){
                        topLevelLayout.setVisibility(View.VISIBLE);
                        hintOne.setVisibility(View.GONE);
                        hintTwo.setVisibility(View.GONE);
                        hintThree.setVisibility(View.VISIBLE);
                        textHelp.setText("Got it");
                        textHelp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.thumb, 0);
                    }
                    if(i>=3){
                        topLevelLayout.setVisibility(View.GONE);
                        hintOne.setVisibility(View.GONE);
                        hintTwo.setVisibility(View.GONE);
                        hintThree.setVisibility(View.GONE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("RanBefore", true);
                        editor.commit();
                    }
                });

                topLevelLayout.setOnClickListener(v -> {
                    i++;
                    if(i==1){
                        topLevelLayout.setVisibility(View.VISIBLE);
                        hintOne.setVisibility(View.GONE);
                        hintTwo.setVisibility(View.VISIBLE);
                        hintThree.setVisibility(View.GONE);
                    }
                    if(i==2){
                        topLevelLayout.setVisibility(View.VISIBLE);
                        hintOne.setVisibility(View.GONE);
                        hintTwo.setVisibility(View.GONE);
                        hintThree.setVisibility(View.VISIBLE);
                        textHelp.setText("Got it");
                        textHelp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.thumb, 0);
                    }
                    if(i>=3){
                        topLevelLayout.setVisibility(View.GONE);
                        hintOne.setVisibility(View.GONE);
                        hintTwo.setVisibility(View.GONE);
                        hintThree.setVisibility(View.GONE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("RanBefore", true);
                        editor.commit();
                    }
                });


            }

        //Set Drawbables
        dFav = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24);
        dNote = ContextCompat.getDrawable(this, R.drawable.ic_baseline_sticky_note_2_24);
        dBook = ContextCompat.getDrawable(this, R.drawable.ic_baseline_bookmark_24);
        dFav.mutate();
        dNote.mutate();
        dBook.mutate();

        //Check if the DEVICE IS ON DARK Mode
        int nightModeFlags =
                this.getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;

        dark = (SharedPreferencesUtil.getDarkMode(sharedPreferences, HomeActivity.this) != null
                && SharedPreferencesUtil.getDarkMode(sharedPreferences, HomeActivity.this).trim()
                .equalsIgnoreCase("TRUE")) ? true : false;

        if (!dark && nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            dark = true;
        }
        //get Theme color
        int[] attrs = {R.attr.colorPrimary};
        TypedArray ta = this.obtainStyledAttributes(attrs);
        int color = ta.getResourceId(0, android.R.color.black);
        ta.recycle();

        if (!dark) {
            DrawableCompat.setTint(dFav, getResources().getColor(color));
            DrawableCompat.setTint(dNote, getResources().getColor(color));
            DrawableCompat.setTint(dBook, getResources().getColor(color));
        } else {
            DrawableCompat.setTint(dFav, Color.WHITE);
            DrawableCompat.setTint(dNote, Color.WHITE);
            DrawableCompat.setTint(dBook, Color.WHITE);
        }
        dFav.setBounds(0, 0, dFav.getIntrinsicWidth() + 15, dFav.getIntrinsicHeight() + 15);
        dNote.setBounds(0, 0, dNote.getIntrinsicWidth() + 15, dNote.getIntrinsicHeight() + 15);
        dBook.setBounds(0, 0, dBook.getIntrinsicWidth() + 15, dBook.getIntrinsicHeight() + 15);

        book = SharedPreferencesUtil.getBook(sharedPreferences, this.getApplicationContext());
        chapter = SharedPreferencesUtil.getChapter(sharedPreferences, this.getApplicationContext());

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Bottom Sheet
        layoutBottom = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottom);

        bv = (TextView) findViewById(R.id.home_verse_text);
        bn = (TextView) findViewById(R.id.home_book);
        be = (TextView) findViewById(R.id.bookenglish);
        iv = (ImageView) findViewById(R.id.bookicon);
        iv.setImageResource(Util.getBibleBookImage(book, this.getApplicationContext()));
        iv2 = (ImageView) findViewById(R.id.outside_image);
        iv.setBackgroundColor(Util.getBookColor(book));
        iv2.setBackgroundColor(Util.getBookOutsideColor(book));
        tb = (TextView) findViewById(R.id.toolbook);
        tl = (TextView) findViewById(R.id.toollang);
        bookBottom = (TextView) findViewById(R.id.bookBottom);

        new Thread() {
            @Override
            public void run() {

                try {
                    //Get all the DB highlights
                    highlightVerses = _helper.getBookHighlightsMap(book, chapter);
                    //Get all Favorites
                    favoriteVerses = _helper.getBookFavoritesMap(book, chapter);
                    //Bookmarks
                    bookmarkVerses = _helper.getBookmarksMap(book, chapter);
                    //Notes
                    notesVerses = _helper.getBibleNotesMap(book, chapter);

                    if(ApplicationData.getLanguages().isEmpty()){
                        //Fill the languages
                        String jsonData = TheWordMetaService.getLanguages(getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE),
                                context);
                        if(jsonData!=null && jsonData.trim().length()>0) {
                            JSONObject obj = new JSONObject(jsonData);
                            JSONArray arr = obj.getJSONArray("bibles");
                            for(int i=0;i<arr.length();i++){
                                JSONObject o = (JSONObject) arr.get(i);
                                String code = o.getString("id");
                                String name = o.getString("language");
                                ApplicationData.addLanguage(code,name);
                            }
                        }
                    }

                    String jsonData = TheWordContentService.getChapter(sharedPreferences, context, book, chapter);
                    if (jsonData != null && jsonData.trim().length() > 0) {
                        jObject = new JSONObject(jsonData);
                        dir = jObject.getString("dir");
                        name = jObject.getString("name");
                        engName = jObject.getString("english");
                        textArray = jObject.getJSONArray("verses");
                        lang = jObject.getString("id");
                        chapter = jObject.getString("chapter");
                        if (name != null && name.length() > 20 && ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
                            toolBook = name.substring(0, 12) + "..." + name.substring(name.length() - 3) + " " + chapter;
                        } else {
                            toolBook = name + " " + chapter;
                        }

                        displayBook = name + " " + chapter;
                        engBook = engName + " " + chapter;
                        for (int i = 0; i < textArray.length(); i++) {
                            JSONObject o = (JSONObject) textArray.get(i);
                            textString = textString + Util.trimToEmpty(o.getString("verse")) + " " + Util.trimToEmpty(o.getString("text")) + System.getProperty("line.separator");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tb.setText(toolBook);
                        tl.setText(ApplicationData.getLanguage(lang));
                        bookBottom.setText(displayBook);

                        if (!SharedPreferencesUtil.getLanguage(sharedPreferences, context).equalsIgnoreCase("ENG")) {
                            be.setText(engBook);
                        }

                        bn.setText(displayBook);
                        bn.setTextDirection(dir.equalsIgnoreCase("LTR") ? TextView.TEXT_DIRECTION_LTR : TextView.TEXT_DIRECTION_RTL);
                        bv.setTextDirection(dir.equalsIgnoreCase("LTR") ? TextView.TEXT_DIRECTION_LTR : TextView.TEXT_DIRECTION_RTL);
                        bookBottom.setTextDirection(dir.equalsIgnoreCase("LTR") ? TextView.TEXT_DIRECTION_LTR : TextView.TEXT_DIRECTION_RTL);
                        bv.setMovementMethod(LongClickMovementMethod.getInstance());
                        bv.setTextSize(SharedPreferencesUtil.getFontSize(sharedPreferences, context));
                        bv.setText(addClickablePart(textString), TextView.BufferType.SPANNABLE);
                        toggleBottomSheet(true);
                    }
                });
            }

        }.start();


        fabRight.setOnClickListener(v -> {
            nextChapter();
        });

        fabLeft.setOnClickListener(v -> {
            previousChapter();
        });

        //Set Swipe Listener - COMMENTING FOR NOW. IT IS INTERFERING WITH OTHER EVENTS
        bv.setOnTouchListener(new SwipeListener(HomeActivity.this) {
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
            Intent i = new Intent(this, LanguageSelectorActivity.class);
            startActivity(i);

        });

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
        v.setText("\u00a9 " + Calendar.getInstance().get(Calendar.YEAR) + " The Digital Word");

        //Bottom Sheet
        bsclose = (ImageView) findViewById(R.id.bsclose);
        bsclose.setOnClickListener(v1 -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                toggleBottomSheet(true);
            }
        });

        //Bottom Sheet Copy
        TextView bsCopy = (TextView) findViewById(R.id.bscopy);
        bsCopy.setOnClickListener(v1 -> {
            String shareText = displayBook + System.getProperty("line.separator") + System.getProperty("line.separator");
            for (String t : selectedText.values()) {
                shareText = shareText + t + System.getProperty("line.separator");
            }
            if (shareText != null && shareText.trim().length() > 0) {
                shareText = shareText + System.getProperty("line.separator") +
                        "https://thedigitalword.org";
            }
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(displayBook, shareText);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(HomeActivity.this, "Selected Verse(s) have been copied",
                    Toast.LENGTH_SHORT).show();
            toggleBottomSheet(true);
        });

        //Bottom Sheet Share
        TextView bsShare = (TextView) findViewById(R.id.bsshare);
        bsShare.setOnClickListener(v1 -> {
            String shareText = "";
            for (String t : selectedText.values()) {
                if (t != null && t.trim().length() > 0) {
                    int ind = t.indexOf(" ");
                    String vs = t.substring(0, ind);
                    String tx = t.substring(ind + 1);
                    shareText = shareText + "<b>" + vs + ".</b> " + tx + " ";
                }
            }
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/*");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Digital Word");
            String message = "<h1><b><span dir ='" + dir + "' >" + displayBook + "</span></b></h1>" +
                    "<p dir='" + dir + "'> " + shareText + "</p><BR>" +
                    "<a href='https://thedigitalword.org'>https://thedigitalword.org</a>";
            shareIntent.putExtra(Intent.EXTRA_TEXT, HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
            startActivity(Intent.createChooser(shareIntent, "Share with:"));
            toggleBottomSheet(true);
        });

        //Bible Notes
        TextView noteB = (TextView) findViewById(R.id.bsnote);
        noteB.setOnClickListener(v1 -> {
            setBibleNote(displayBook + ":");
        });

        //Bookmarks
        TextView bookB = (TextView) findViewById(R.id.bsbook);
        bookB.setOnClickListener(v1 -> {
            setBookmarks(displayBook + ":");
        });

        //Favorites
        TextView favB = (TextView) findViewById(R.id.bsfav);
        favB.setOnClickListener(v1 -> {
            setFavVerses();
        });

        //Highlights
        ImageView h1 = (ImageView) findViewById(R.id.CFFFF2E);
        ImageView h2 = (ImageView) findViewById(R.id.ffa726);
        ImageView h3 = (ImageView) findViewById(R.id.C97E47E);
        ImageView h4 = (ImageView) findViewById(R.id.CB7D0E1);
        ImageView h5 = (ImageView) findViewById(R.id.CCE9DD9);
        ImageView h6 = (ImageView) findViewById(R.id.CE5DB9C);
        hdelete = (ImageView) findViewById(R.id.highcolordelete);
        colorLayout = (LinearLayout) findViewById(R.id.colorlinear);

        if (ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
            FrameLayout.LayoutParams params = new
                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            colorLayout.setLayoutParams(params);
        } else {
            FrameLayout.LayoutParams params = new
                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            colorLayout.setLayoutParams(params);
        }

        h1.setOnClickListener(v1 -> {
            highlightVerse("#FFFF2E");
        });
        h2.setOnClickListener(v1 -> {
            highlightVerse("#ffa726");
        });
        h3.setOnClickListener(v1 -> {
            highlightVerse("#97E47E");
        });
        h4.setOnClickListener(v1 -> {
            highlightVerse("#B7D0E1");
        });
        h5.setOnClickListener(v1 -> {
            highlightVerse("#CE9DD9");
        });
        h6.setOnClickListener(v1 -> {
            highlightVerse("#E5DB9C");
        });
        hdelete.setOnClickListener(v1 -> {
            deleteHighLights();
        });

        //Set callbacks
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    toggleBottomSheet(false);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    toggleBottomSheet(true);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    toggleBottomSheet(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }else{
            Util.onFatalException(getApplicationContext());
        }
    }

    String newBook = "";
    String newChapter = "";

    private void previousChapter(){
        try{
            toggleBottomSheet(true);
            String book = SharedPreferencesUtil.getBook(sharedPreferences,this.getApplicationContext());
            String chapter = SharedPreferencesUtil.getChapter(sharedPreferences,this.getApplicationContext());

            new Thread(){
                @Override
                public void run() {
                    try {
                        String jsonData = TheWordContentService.getPreviousChapter(sharedPreferences,context,book,chapter);

                        if(jsonData!=null && jsonData.trim().length()>0) {
                            textString = "";
                            jObject = new JSONObject(jsonData);
                            dir = jObject.getString("dir");
                            name = jObject.getString("name");
                            engName = jObject.getString("english");
                            textArray = jObject.getJSONArray("verses");
                            newBook = jObject.getString("number");
                            newChapter = jObject.getString("chapter");
                            lang = jObject.getString("id");
                            if (name != null && name.length() > 20 && ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
                                toolBook = name.substring(0, 12) + "..." + name.substring(name.length() - 3) + " " + newChapter;
                            } else {
                                toolBook = name + " " + newChapter;
                            }
                            displayBook = name + " " + newChapter;
                            engBook = engName + " " + newChapter;
                            //Set the new Book and Chapter
                            SharedPreferencesUtil.setBook(sharedPreferences, context, newBook);
                            SharedPreferencesUtil.setChapter(sharedPreferences, context, newChapter);
                            for (int i = 0; i < textArray.length(); i++) {
                                JSONObject o = (JSONObject) textArray.get(i);
                                textString = textString + Util.trimToEmpty(o.getString("verse")) + " " + Util.trimToEmpty(o.getString("text")) + System.getProperty("line.separator");
                            }
                        }
                        //Get all the DB highlights
                        highlightVerses = _helper.getBookHighlightsMap(newBook,newChapter);
                        //Get all Favorites
                        favoriteVerses = _helper.getBookFavoritesMap(newBook,newChapter);
                        //Bookmarks
                        bookmarkVerses = _helper.getBookmarksMap(newBook,newChapter);
                        //Notes
                        notesVerses = _helper.getBibleNotesMap(newBook,newChapter);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //refresh
                            iv.setImageResource(Util.getBibleBookImage(newBook,context));
                            iv.setBackgroundColor(Util.getBookColor(newBook));
                            iv2.setBackgroundColor(Util.getBookOutsideColor(newBook));
                            bn.setText(displayBook);
                            if(!SharedPreferencesUtil.getLanguage(sharedPreferences,context).equalsIgnoreCase("ENG")){
                                be.setText(engBook);
                            }
                            bookBottom.setText(displayBook);
                            tb.setText(toolBook);
                            tl.setText(ApplicationData.getLanguage(lang));
                            bn.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                            bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                            bookBottom.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                            bv.setMovementMethod(LongClickMovementMethod.getInstance());
                            bv.setText(addClickablePart(textString), TextView.BufferType.SPANNABLE);
                            bv.setTextSize(SharedPreferencesUtil.getFontSize(sharedPreferences,context));
                        }
                    });
                }
            }.start();
        }catch(Exception e){

        }
    }

    private void nextChapter(){
        try{
            toggleBottomSheet(true);
            String book = SharedPreferencesUtil.getBook(sharedPreferences,this.getApplicationContext());
            String chapter = SharedPreferencesUtil.getChapter(sharedPreferences,this.getApplicationContext());

            new Thread(){
                @Override
                public void run() {
                    try {
                        String jsonData = TheWordContentService.getNextChapter(sharedPreferences,context,book,chapter);
                        if(jsonData!=null && jsonData.trim().length()>0) {
                            textString = "";
                            jObject = new JSONObject(jsonData);
                            dir = jObject.getString("dir");
                            name = jObject.getString("name");
                            engName = jObject.getString("english");
                            textArray = jObject.getJSONArray("verses");
                            newBook = jObject.getString("number");
                            newChapter = jObject.getString("chapter");
                            lang = jObject.getString("id");
                            if (name != null && name.length() > 20 && ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
                                toolBook = name.substring(0, 12) + "..." + name.substring(name.length() - 3) + " " + newChapter;
                            } else {
                                toolBook = name + " " + newChapter;
                            }
                            displayBook = name + " " + newChapter;
                            engBook = engName + " " + newChapter;
                            //Set the new Book and Chapter
                            SharedPreferencesUtil.setBook(sharedPreferences, context, newBook);
                            SharedPreferencesUtil.setChapter(sharedPreferences, context, newChapter);
                            for (int i = 0; i < textArray.length(); i++) {
                                JSONObject o = (JSONObject) textArray.get(i);
                                textString = textString + Util.trimToEmpty(o.getString("verse")) + " " + Util.trimToEmpty(o.getString("text")) + System.getProperty("line.separator");
                            }
                            //Get all the DB highlights
                            highlightVerses = _helper.getBookHighlightsMap(newBook,newChapter);
                            //Get all Favorites
                            favoriteVerses = _helper.getBookFavoritesMap(newBook,newChapter);
                            //Bookmarks
                            bookmarkVerses = _helper.getBookmarksMap(newBook,newChapter);
                            //Notes
                            notesVerses = _helper.getBibleNotesMap(newBook,newChapter);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //Update UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //refresh
                            iv.setImageResource(Util.getBibleBookImage(newBook,context));
                            iv.setBackgroundColor(Util.getBookColor(newBook));
                            iv2.setBackgroundColor(Util.getBookOutsideColor(newBook));
                            if(!SharedPreferencesUtil.getLanguage(sharedPreferences,context).equalsIgnoreCase("ENG")){
                                be.setText(engBook);
                            }
                            bn.setText(displayBook);
                            bookBottom.setText(displayBook);
                            tb.setText(toolBook);
                            tl.setText(ApplicationData.getLanguage(lang));
                            bn.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                            bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                            bookBottom.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
                            bv.setMovementMethod(LongClickMovementMethod.getInstance());
                            bv.setText(addClickablePart(textString), TextView.BufferType.SPANNABLE);
                            bv.setTextSize(SharedPreferencesUtil.getFontSize(sharedPreferences,context));
                        }
                    });
                }
            }.start();
        }catch(Exception e){

        }
    }

    private SpannableStringBuilder addClickablePart(String str) {
        final String nonBreakingSpace = "\u00A0";
        str = str.replace(System.getProperty("line.separator"), nonBreakingSpace + nonBreakingSpace + nonBreakingSpace +nonBreakingSpace + nonBreakingSpace + nonBreakingSpace + System.getProperty("line.separator"));
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        if(str!=null && str.trim().length()>0) {
            String[] strings = str.split(System.getProperty("line.separator"));

            for (String stringPart : strings) {
                int start = str.indexOf(stringPart);
                int end = stringPart.length() + start;
                int imageend = end-5;
                String verse = stringPart.split(" ")[0];
                ssb.setSpan(new StyleSpan(Typeface.BOLD), start, start + stringPart.split(" ")[0].length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(0.8f), start,start + stringPart.split(" ")[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                ssb.setSpan(new LongClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(dark?Color.WHITE:Color.BLACK);
                        ds.setUnderlineText(false);
                    }

                    @Override
                    public void onLongClick(View view) {
                        toggleBottomSheet(true);
                        String verse = stringPart.split(" ")[0];
                        String code = SharedPreferencesUtil.getBook(sharedPreferences,context) + " "
                                + SharedPreferencesUtil.getChapter(sharedPreferences,context)+":"+verse;
                        String bookName = "";
                        if(!SharedPreferencesUtil.getLanguage(sharedPreferences,context).equalsIgnoreCase("ENG")){
                            bookName = "("+engName+") "+name;
                        }else{
                            bookName = name;
                        }
                        try {
                            fm = getSupportFragmentManager();
                            String title = bookName + " "
                                    + SharedPreferencesUtil.getChapter(sharedPreferences, context) + ":" + verse;
                            CrossRefDialog crossDialogFragment = CrossRefDialog.newInstance(code, title);

                            crossDialogFragment.show(fm, CrossRefDialog.TAG);
                        }catch (Exception e){
                            finish();
                            startActivity(getIntent());
                        }
                    }

                    @Override
                    public void onClick(View widget) {
                        String[] splitString = stringPart!=null?(stringPart.split(" ")):null;
                        String verse = splitString!=null?(splitString[0]):"0";
                        String text = (splitString!=null && splitString.length>1)?(splitString[1]):"";
                        if(text!=null && text.trim().length()>0) {
                            toggleHighLightedSpan((TextView) widget, stringPart);
                            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                toggleBottomSheet(false);
                            }
                        }
                    }
                }, start, end-6, 0);

                //Highlights
                if(highlightVerses!=null && highlightVerses.size()>0){
                    if(highlightVerses.keySet().contains(verse)){
                        BibleDBContent model = highlightVerses.get(verse);
                        ssb.setSpan(new BackgroundColorSpan(Color.parseColor(model.getColor())), start,end-6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        if(dark){
                            ssb.setSpan(new ForegroundColorSpan(Color.BLACK), start,end-6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                    }
                }
                //Favorites
                if(favoriteVerses!=null && favoriteVerses.size()>0){
                    if(favoriteVerses.keySet().contains(verse)) {
                        BibleDBContent model = favoriteVerses.get(verse);
                        ImageSpan span = new ImageSpan(dFav , ImageSpan.ALIGN_BASELINE);
                        ssb.setSpan(span, imageend, imageend+1 , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        ssb.setSpan(new ClickableSpan() {
                                       @Override
                                       public void onClick(View widget) {
                                           toggleBottomSheet(true);
                                           new AlertDialog.Builder(HomeActivity.this)
                                                   .setMessage("Do you want to delete this verse from your favorites?")
                                                   .setTitle("Favorite Verse")
                                                   .setCancelable(false)
                                                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                       public void onClick(DialogInterface dialog, int id) {
                                                           BibleHelper _noteHelper = new BibleHelper(HomeActivity.this);
                                                           BibleDBContent model = favoriteVerses.get(verse);
                                                           _noteHelper.deleteFavorite(model);
                                                           Intent data = new Intent(context, HomeActivity.class);
                                                           // Request MainActivity refresh its ListView (or not).
                                                           data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           // Set Result
                                                           context.startActivity(data);
                                                       }
                                                   })
                                                   .setNegativeButton("No", null)
                                                   .show();
                                       }
                                   }, imageend, imageend+1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        imageend = imageend +2;
                    }
                }

                //Bookmarks
                if(bookmarkVerses!=null && bookmarkVerses.size()>0){
                    Iterator iter = bookmarkVerses.keySet().iterator();
                    boolean verseHasBookmark = false;
                    while (iter.hasNext()){
                        verseHasBookmark =
                                Arrays.asList(((String)iter.next()).split(",",-1)).contains(verse);
                        if(verseHasBookmark){
                            break;
                        }
                    }
                    //If verse has a bookmark, identify it
                    if(verseHasBookmark) {
                        ImageSpan span = new ImageSpan(dBook, ImageSpan.ALIGN_BASELINE);
                        ssb.setSpan(span, imageend, imageend + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        //Set Bookmark Clickable Span
                        ssb.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(@NonNull View widget) {
                                            toggleBottomSheet(true);
                                            Intent data = new Intent(context, HomeBookmarkActivity.class);
                                            data.putExtra("verse",verse);
                                            data.putExtra("bookName",name);
                                            data.putExtra("bookEng",engName);
                                            startActivity(data);
                                        }
                                    }, imageend, imageend+1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        imageend = imageend + 2;
                    }
                }

                //Bible Notes
                if(notesVerses!=null && notesVerses.size()>0){
                    Iterator iter = notesVerses.keySet().iterator();
                    boolean verseHasNote = false;
                    while (iter.hasNext()){
                        verseHasNote =
                                Arrays.asList(((String)iter.next()).split(",",-1)).contains(verse);
                        if(verseHasNote){
                            break;
                        }
                    }
                    //If verse has a Note, identify it
                    if(verseHasNote) {
                        ImageSpan span = new ImageSpan(dNote, ImageSpan.ALIGN_BASELINE);
                        ssb.setSpan(span, imageend, imageend + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        //Set Clickable Span
                        ssb.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(@NonNull View widget) {
                                            toggleBottomSheet(true);
                                            Intent data = new Intent(context, HomeNotesActivity.class);
                                            data.putExtra("verse",verse);
                                            data.putExtra("bookName",name);
                                            data.putExtra("bookEng",engName);
                                            startActivity(data);
                                        }
                                    }, imageend, imageend+1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        imageend = imageend + 2;
                    }
                }


               // if(verse!=null && verse.equalsIgnoreCase("8")){
                    //ssb.setSpan(new BackgroundColorSpan(Color.parseColor("#CE9DD9")), start,end-6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    //Add an image
//                    ImageSpan span = new ImageSpan(dFav , ImageSpan.ALIGN_BASELINE);
//                    ssb.setSpan(span, imageend, imageend+1 , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    ssb.setSpan(new ClickableSpan() {
//                                   @Override
//                                   public void onClick(View widget) {
//                                       toggleBottomSheet(true);
//                                       Toast.makeText(HomeActivity.this, "Favorite for verse: "+verse,
//                                               Toast.LENGTH_SHORT).show();
//                                   }
//                               }, imageend, imageend+1,
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    imageend = imageend +2;

//                    ImageSpan spand = new ImageSpan(dBook , ImageSpan.ALIGN_BASELINE);
//                    ssb.setSpan(spand, imageend, imageend+1 , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    ssb.setSpan(new ClickableSpan() {
//                                    @Override
//                                    public void onClick(View widget) {
//                                        toggleBottomSheet(true);
//                                        Toast.makeText(HomeActivity.this, "Bookmart for verse: "+verse,
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }, imageend, imageend+1,
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    imageend = imageend +2;
//
//                    ImageSpan spann = new ImageSpan(dNote , ImageSpan.ALIGN_BASELINE);
//                    ssb.setSpan(spann, imageend, imageend +1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    ssb.setSpan(new ClickableSpan() {
//                                    @Override
//                                    public void onClick(View widget) {
//                                        toggleBottomSheet(true);
//                                        Toast.makeText(HomeActivity.this, "Note for verse: "+verse,
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }, imageend, imageend+1,
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                }

            }
        }
        return ssb;
    }

    public void toggleHighLightedSpan(TextView tv, String textToHighlight) {
        String tvt = tv.getText().toString().trim().toUpperCase();
        String verse = textToHighlight!=null?(textToHighlight.split(" ")[0]):"0";
        Integer v = Integer.valueOf(verse);
        int ofe = tvt.indexOf(textToHighlight.toUpperCase(), 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight.toUpperCase(), ofs);
            if (ofe == -1)
                break;
            else {
                if(selectedText.keySet().contains(v)){
                    ColoredUnderlineSpan[] removeTextSpan = wordToSpan.getSpans(ofe,ofe+textToHighlight.length()-6,ColoredUnderlineSpan.class);
                    for (int i = 0; i < removeTextSpan.length; i++) {
                        wordToSpan.removeSpan(removeTextSpan[i]);
                        tv.setText(wordToSpan);
                    }
                    selectedText.remove(v);
                    if(selectedText.isEmpty()){
                        toggleBottomSheet(true);
                    }
                }else {
                    selectedText.put(v,textToHighlight);
                    wordToSpan.setSpan(new ColoredUnderlineSpan(Color.RED), ofe, ofe + textToHighlight.length()-6 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
                }
                //Decide to show delete
                if(highlightVerses!=null && !highlightVerses.isEmpty()) {
                    Set keys = highlightVerses.keySet();
                    boolean hasADelete = false;
                    Iterator i = selectedText.keySet().iterator();
                    while (i.hasNext()){
                        if(keys.contains(String.valueOf((Integer)i.next()))){
                            hasADelete = true;
                            break;
                        }
                    }
                    if(hasADelete){
                        hdelete.setVisibility(View.VISIBLE);
                    }else{
                        hdelete.setVisibility(View.GONE);
                    }
                }else{
                    hdelete.setVisibility(View.GONE);
                }
            }
        }
    }

    private void toggleBottomSheet(boolean isClose){
        if(isClose) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            CoordinatorLayout.LayoutParams left = (CoordinatorLayout.LayoutParams) fabLeft.getLayoutParams();
            CoordinatorLayout.LayoutParams right = (CoordinatorLayout.LayoutParams) fabRight.getLayoutParams();
            //left.setAnchorId(View.NO_ID);
            left.width = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
            left.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
            fabLeft.setLayoutParams(left);
            fabLeft.show();
            //right.setAnchorId(R.id.appBarLayout);
            right.width = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
            right.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
            fabRight.setLayoutParams(right);
            fabRight.show();
            navigationView.setVisibility(View.VISIBLE);
            //Remove all Spans
            SpannableString ss=(SpannableString)bv.getText();
            ColoredUnderlineSpan[] spans=ss.getSpans(0, bv.getText().length()-6, ColoredUnderlineSpan.class);
            for(int i=0; i<spans.length; i++){
                ss.removeSpan(spans[i]);
            }
            //Clear list
            selectedText.clear();
        }else{
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //Hide FAB and BottomNav
            CoordinatorLayout.LayoutParams left = (CoordinatorLayout.LayoutParams)fabLeft.getLayoutParams();
           // left.setAnchorId(View.NO_ID);
            left.width = 0;
            left.height = 0;
            fabLeft.setLayoutParams(left);
            fabLeft.hide();
            CoordinatorLayout.LayoutParams right = (CoordinatorLayout.LayoutParams)fabRight.getLayoutParams();
            //right.setAnchorId(View.NO_ID);
            right.width = 0;
            right.height = 0;
            fabRight.setLayoutParams(right);
            fabRight.hide();
            navigationView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void deleteHighLights(){
        if(selectedText!=null && !selectedText.keySet().isEmpty()){
            Iterator i = selectedText.keySet().iterator();
            List<String> toDelete = new ArrayList<>();
            while (i.hasNext()) {
                toDelete.add(String.valueOf((Integer) i.next()));
            }
            BibleHelper helper = new BibleHelper(this);
            //Delete Existing
            if(!toDelete.isEmpty()){
                boolean after_first_string = false;
                StringBuffer sb = new StringBuffer("(");
                for(String s: toDelete) {
                    if (after_first_string) {
                        sb.append(",");
                    }
                    after_first_string = true;
                    sb.append("'").append(s.replace("'","''")).append("'");
                }
                sb.append(")");
                helper.deleteHighlights(sb.toString());
                Toast.makeText(HomeActivity.this, "Highlight successfully applied.",
                        Toast.LENGTH_SHORT).show();
                Intent data = new Intent(context,HomeActivity.class);
                // Request MainActivity refresh its ListView (or not).
                data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Set Result
                startActivity(data);
            }
        }
    }
    public void highlightVerse(String color){
        if(selectedText!=null && !selectedText.keySet().isEmpty()){
            List<BibleDBContent> models = new ArrayList<BibleDBContent>();
            Iterator i = selectedText.keySet().iterator();
            //Existing values
            Set existingHighlights = highlightVerses.keySet();
            List<String> toDelete = new ArrayList<>();
            while (i.hasNext()){
                Integer verse = (Integer) i.next();
                if(existingHighlights.contains(String.valueOf(verse))){
                    toDelete.add(String.valueOf(verse));
                }
                BibleDBContent model = new BibleDBContent();
                model.setColor(color);
                model.setBibleVerse(String.valueOf(verse));
                model.setBibleChapter(SharedPreferencesUtil.getChapter(sharedPreferences,getApplicationContext()));
                model.setBibleBook(SharedPreferencesUtil.getBook(sharedPreferences,getApplicationContext()));
                model.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                models.add(model);
            }
            BibleHelper helper = new BibleHelper(this);
            //Delete Existing
            if(!toDelete.isEmpty()){
                boolean after_first_string = false;
                StringBuffer sb = new StringBuffer("(");
                for(String s: toDelete) {
                    if (after_first_string) {
                        sb.append(",");
                    }
                    after_first_string = true;
                    sb.append("'").append(s.replace("'","''")).append("'");
                }
                sb.append(")");
                helper.deleteHighlights(sb.toString());
            }

            //Add new
            helper.addHighlights(models);
            Toast.makeText(HomeActivity.this, "Highlight successfully applied.",
                    Toast.LENGTH_SHORT).show();
            Intent data = new Intent(context,HomeActivity.class);
            // Request MainActivity refresh its ListView (or not).
            data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            // Set Result
            startActivity(data);
        }
    }

    public void setBookmarks(String displayBook){
        //Close the bottom sheet

        AddBookmarkDialog addBookmarkDialog = null;
        String verses = "";
        try {
            fm = getSupportFragmentManager();
            //find the selected verses
            if(selectedText!=null && !selectedText.keySet().isEmpty()) {
                List keys = new ArrayList(selectedText.keySet());
                Collections.sort(keys);
                Iterator i = keys.iterator();
                //Existing values
                while (i.hasNext()) {
                    Integer verse = (Integer) i.next();
                    verses = verses + verse +",";
                }
                if(verses.endsWith(",")){
                    verses = verses.substring(0,verses.lastIndexOf(","));
                }
                addBookmarkDialog = AddBookmarkDialog.newInstance(displayBook,verses);
                addBookmarkDialog.show(fm, AddBookmarkDialog.TAG);
                toggleBottomSheet(true);
            }
        }catch (Exception e){
            addBookmarkDialog.dismiss();
            toggleBottomSheet(true);
        }
    }

    public void setBibleNote(String displayBook){
        //Close the bottom sheet

        AddBibleNoteDialog addBibleNoteDialogDialog = null;
        String verses = "";
        try {
            fm = getSupportFragmentManager();
            //find the selected verses
            if(selectedText!=null && !selectedText.keySet().isEmpty()) {
                List keys = new ArrayList(selectedText.keySet());
                Collections.sort(keys);
                Iterator i = keys.iterator();
                //Existing values
                while (i.hasNext()) {
                    Integer verse = (Integer) i.next();
                    verses = verses + verse +",";
                }
                if(verses.endsWith(",")){
                    verses = verses.substring(0,verses.lastIndexOf(","));
                }
                addBibleNoteDialogDialog = AddBibleNoteDialog.newInstance(displayBook,verses);
                addBibleNoteDialogDialog.show(fm, AddBookmarkDialog.TAG);
                toggleBottomSheet(true);
            }
        }catch (Exception e){
            addBibleNoteDialogDialog.dismiss();
            toggleBottomSheet(true);
        }
    }

    public void setFavVerses(){
        if(selectedText!=null && !selectedText.keySet().isEmpty()){
            List<BibleDBContent> models = new ArrayList<BibleDBContent>();
            Iterator i = selectedText.keySet().iterator();
            //Existing values
            Set existingFavs = favoriteVerses.keySet();
            List<String> toDelete = new ArrayList<>();
            while (i.hasNext()){
                Integer verse = (Integer) i.next();
                if(!existingFavs.contains(String.valueOf(verse))){
                    BibleDBContent model = new BibleDBContent();
                    model.setBibleVerse(String.valueOf(verse));
                    model.setBibleChapter(SharedPreferencesUtil.getChapter(sharedPreferences,getApplicationContext()));
                    model.setBibleBook(SharedPreferencesUtil.getBook(sharedPreferences,getApplicationContext()));
                    model.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    models.add(model);
                }
            }
            BibleHelper helper = new BibleHelper(this);
            //Add new
            helper.addFavorites(models);
            Toast.makeText(HomeActivity.this, "Favorites successfully saved.",
                    Toast.LENGTH_SHORT).show();
            Intent data = new Intent(context,HomeActivity.class);
            // Request MainActivity refresh its ListView (or not).
            data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            // Set Result
            startActivity(data);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        finish();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }


}


