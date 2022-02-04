package com.theword.thedigitalword;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.theword.thedigitalword.component.ColoredUnderlineSpan;
import com.theword.thedigitalword.model.Book;
import com.theword.thedigitalword.notification.AlarmReceiver;
import com.theword.thedigitalword.notification.NotificationScheduler;
import com.theword.thedigitalword.service.TheWordMetaService;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

public class MeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    BottomNavigationView navigationView = null;
    Context context = null;

    //BottomSheet
    LinearLayout layoutBottom = null;
    BottomSheetBehavior sheetBehavior = null;
    ImageView bsclose = null;
    Button okB = null;
    TimePicker timePicker = null;
    TextView timetv = null;
    String hour;
    String minute;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        context = getApplicationContext();

        //Get Books Map
        //Process on separate Thread
        new Thread(){
            @Override
            public void run() {
                try {
                    String jsonData = TheWordMetaService.getBooks(sharedPreferences,
                            context);
                    if(jsonData!=null && jsonData.trim().length()>0) {
                        //Clear Books
                        ApplicationData.clearBooks();
                        JSONObject obj = new JSONObject(jsonData);
                        String dir = obj.getString("direction");
                        String lang = obj.getString("id");
                        JSONArray arr = obj.getJSONArray("books");
                        for(int i=0;i<arr.length();i++){
                            JSONObject o = (JSONObject) arr.get(i);
                            String name = o.getString("name");
                            String english = o.getString("english");
                            String id = o.getString("number");
                            ApplicationData.addBook(id,name);
                            ApplicationData.addEngBook(id,english);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

        //Bottom Sheet
        layoutBottom = (LinearLayout) findViewById(R.id.bottom_time);
        sheetBehavior = BottomSheetBehavior.from(layoutBottom);
        bsclose = (ImageView)findViewById(R.id.bstclose);
        okB = (Button)findViewById(R.id.timebutton);
        timePicker = (TimePicker)findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);
        timetv = (TextView)findViewById(R.id.pushrt);
        TextView v11 = (TextView)findViewById(R.id.me_footer1);
        v11.setText("\u00a9 "+ Calendar.getInstance().get(Calendar.YEAR)+" The Digital Word");

        hour = SharedPreferencesUtil.getRemHour(sharedPreferences,getApplicationContext());
        minute = SharedPreferencesUtil.getRemMin(sharedPreferences,getApplicationContext());

        SeekBar sb = (SeekBar)findViewById(R.id.textSize);
        TextView pv = (TextView)findViewById(R.id.changeFont);
        ImageView res = (ImageView)findViewById(R.id.restore);
        res.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            SharedPreferencesUtil.saveFontSize(sharedPreferences,MeActivity.this,20);
            pv.setTextSize(20);
            sb.setProgress(20);
        });
        pv.setTextSize(SharedPreferencesUtil.getFontSize(sharedPreferences,this));
        sb.setProgress(SharedPreferencesUtil.getFontSize(sharedPreferences,this));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                toggleBottomSheet(true,false);
                int prog = progress;
                if(progress<10){
                    prog = 10;
                }
                SharedPreferencesUtil.saveFontSize(sharedPreferences,MeActivity.this,prog);
                pv.setTextSize(prog);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Switch d = (Switch)findViewById(R.id.dark);
        d.setChecked(
                (SharedPreferencesUtil.getDarkMode(sharedPreferences,MeActivity.this)!=null
                        && SharedPreferencesUtil.getDarkMode(sharedPreferences,MeActivity.this).trim()
                        .equalsIgnoreCase("TRUE"))?true:false
        );
        d.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.saveDarkMode(sharedPreferences,MeActivity.this, "true");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    SharedPreferencesUtil.saveDarkMode(sharedPreferences,MeActivity.this, "false");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        Switch sw = (Switch) findViewById(R.id.pushs);
        sw.setChecked(
                (SharedPreferencesUtil.getPushNotification(sharedPreferences,MeActivity.this)!=null
                        && SharedPreferencesUtil.getPushNotification(sharedPreferences,MeActivity.this).trim()
                        .equalsIgnoreCase("TRUE"))?true:false
        );
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleBottomSheet(true,false);
                if (isChecked) {
                    SharedPreferencesUtil.saveNotification(sharedPreferences,MeActivity.this, "true");
                    NotificationScheduler.setReminder(getApplicationContext());
                } else {
                    SharedPreferencesUtil.saveNotification(sharedPreferences,MeActivity.this, "false");
                    NotificationScheduler.cancelReminder(getApplicationContext());
                }
            }
        });

        Switch st = (Switch)findViewById(R.id.pushr);
        st.setChecked(
                (SharedPreferencesUtil.getRemNotification(sharedPreferences,MeActivity.this)!=null
                        && SharedPreferencesUtil.getRemNotification(sharedPreferences,MeActivity.this).trim()
                        .equalsIgnoreCase("TRUE"))?true:false
        );

        if(st.isChecked()) {
            timetv.setVisibility(View.VISIBLE);
        }else {
            timetv.setVisibility(View.INVISIBLE);
        }
        timetv.setText((hour.length()<2?"0"+hour:hour) +":"+ (minute.length()<2?"0"+minute:minute));

        st.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.saveRemNotification(sharedPreferences,MeActivity.this, "true");
                    NotificationScheduler.setRemReminder(getApplicationContext());
                    timetv.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferencesUtil.saveRemNotification(sharedPreferences,MeActivity.this, "false");
                    NotificationScheduler.cancelRemReminder(getApplicationContext());
                    timetv.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Theme Settings
        ImageView g = (ImageView)findViewById(R.id.grayT);
        ImageView p = (ImageView)findViewById(R.id.pinkT);
        ImageView r = (ImageView)findViewById(R.id.redT);
        ImageView pr = (ImageView)findViewById(R.id.purpleT);
        ImageView b = (ImageView)findViewById(R.id.blueT);
        ImageView t = (ImageView)findViewById(R.id.tealT);
        ImageView gt = (ImageView)findViewById(R.id.greenT);
        ImageView y = (ImageView)findViewById(R.id.yellowT);
        ImageView o = (ImageView)findViewById(R.id.orangeT);

        y.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_YELLOW);
        });
        o.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_ORANGE);
        });
        gt.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_GREEN);
        });
        t.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_TEAL);
        });
        b.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_BLUE);
        });
        g.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord);
        });
        p.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_PINK);
        });
        r.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_RED);
        });
        pr.setOnClickListener(v -> {
            toggleBottomSheet(true,false);
            setTheme(sharedPreferences,this,R.style.Theme_TheDigitalWord_PURPLE);
        });


        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_read:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        break;
                    case R.id.action_today:
                        startActivity(new Intent(getApplicationContext(), TodayActivity.class));
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
        navigationView.setSelectedItemId(R.id.action_me);

        timetv.setOnClickListener(v -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                toggleBottomSheet(false,false);
            }
        });

        bsclose.setOnClickListener(v1 -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                toggleBottomSheet(true,false);
            }
        });

        okB.setOnClickListener(v -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                toggleBottomSheet(true,true);
            }
        });

        //Set callbacks
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    toggleBottomSheet(false,false);
                }
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    toggleBottomSheet(true,false);
                }
                else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    toggleBottomSheet(true,false);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

         /** Bible Activities **/
        //Personal Notes
        TextView personalNotes = findViewById(R.id.mepnote);
        personalNotes.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PersonalNoteActivity.class));
        });

        //Highlights
        TextView high = findViewById(R.id.mehigh);
        high.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), HightlightActivity.class));
        });

        //Favorites
        TextView fav = findViewById(R.id.mefav);
        fav.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
        });

        //Notes
        TextView notes = findViewById(R.id.menote);
        notes.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), NotesActivity.class));
        });

        //Bookmarks
        TextView book = findViewById(R.id.mebook);
        book.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
        });

    }

    private void toggleBottomSheet(boolean isClose, boolean saveState){
        if(isClose) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            navigationView.setVisibility(View.VISIBLE);
            if(saveState) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                timetv.setText((hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute));
                SharedPreferencesUtil.saveRemHour(sharedPreferences, getApplicationContext(), String.valueOf(hour));
                SharedPreferencesUtil.saveRemMin(sharedPreferences, getApplicationContext(), String.valueOf(minute));
                NotificationScheduler.setRemReminder(getApplicationContext());
            }
        }else{
            int H = Integer.valueOf(SharedPreferencesUtil.getRemHour(sharedPreferences,getApplicationContext()));
            int M = Integer.valueOf(SharedPreferencesUtil.getRemMin(sharedPreferences,getApplicationContext()));
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            timePicker.setCurrentHour(H);
            timePicker.setCurrentMinute(M);
            navigationView.setVisibility(View.INVISIBLE);
        }
    }

    private void setTheme(SharedPreferences sharedPreferences, Context context, int theme){
        SharedPreferencesUtil.saveTheme(sharedPreferences,context, theme);
        Toast.makeText(MeActivity.this, "The Selected Theme Has Been Applied",
                                Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
        finishAffinity();
    }
}
