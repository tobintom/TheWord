package com.theword.thedigitalword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.theword.thedigitalword.adapter.HighlightAdapter;
import com.theword.thedigitalword.adapter.PersonalNoteAdapter;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.model.PersonalNote;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HightlightActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    TextView back = null;
    TextView highs = null;
    private RecyclerView srchView = null;
    private List<BibleDBContent> srchList = null;
    Context context = null;
    private static final int MY_REQUEST_CODE = 200;
    HighlightAdapter srchAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences, this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hightlight);
        context = this.getApplicationContext();
        srchList = new ArrayList<>();
        srchView = findViewById(R.id.idhighlights);

        highs = findViewById(R.id.highlights);
        //Go Back
        back = findViewById(R.id.highlightBack);
        back.setOnClickListener(v -> {
            finish();
        });
        //Get all data
        BibleHelper _helper = new BibleHelper(this);
        srchList = _helper.getAllHighlights();
        if(srchList==null || srchList.size()==0){
            highs.setText("No verses found..");
        }else {
            highs.setText("Click on list icon to read full chapter.");
            srchAdapter = new HighlightAdapter(HightlightActivity.this,srchList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            srchView.setLayoutManager(linearLayoutManager);
            srchView.setAdapter(srchAdapter);
        }
    }
}