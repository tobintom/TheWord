package com.theword.thedigitalword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.theword.thedigitalword.adapter.FavAdapter;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    TextView back = null;
    TextView favs = null;
    private RecyclerView srchView = null;
    private List<BibleDBContent> srchList = null;
    Context context = null;
    private static final int MY_REQUEST_CODE = 201;
    FavAdapter srchAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences, this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        context = this.getApplicationContext();
        srchList = new ArrayList<>();

        srchView = findViewById(R.id.idfavs);
        favs = findViewById(R.id.favs);

        //Go Back
        back = findViewById(R.id.favBack);
        back.setOnClickListener(v -> {
            finish();
        });

        //Get all data
        BibleHelper _helper = new BibleHelper(this);
        srchList = _helper.getAllFavorites();
        if(srchList==null || srchList.size()==0){
            favs.setText("No verses found..");
        }else {
            favs.setText("Click on list icon to read full chapter.");
            srchAdapter = new FavAdapter(FavoriteActivity.this,srchList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            srchView.setLayoutManager(linearLayoutManager);
            srchView.setAdapter(srchAdapter);
        }
    }
}