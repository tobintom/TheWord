package com.theword.thedigitalword;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.Calendar;

public class ExceptionActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;

    TextView f = null;
    Button b = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences, this));
        super.onCreate(savedInstanceState);
        Context context = this.getApplicationContext();
        setContentView(R.layout.activity_exception);
        b = (Button)findViewById(R.id.eclose);
        f = (TextView) findViewById(R.id.back_footer1);
        f.setText("\u00a9 "+ Calendar.getInstance().get(Calendar.YEAR)+" The Digital Word");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
}