package com.theword.thedigitalword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theword.thedigitalword.adapter.HomeBookmarkAdapter;
import com.theword.thedigitalword.db.BibleHelper;
import com.theword.thedigitalword.model.BibleDBContent;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HomeBookmarkActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    TextView back = null;
    TextView favs = null;
    TextView title = null;
    private RecyclerView srchView = null;
    private List<BibleDBContent> srchList = null;
    Context context = null;
    private static final int MY_REQUEST_CODE = 209;
    HomeBookmarkAdapter srchAdapter = null;
    private String book = null;
    private String chapter = null;
    private String verse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this, SharedPreferencesUtil.getTheme(sharedPreferences, this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homebookmark);

        book = SharedPreferencesUtil.getBook(sharedPreferences,this.getApplicationContext());
        chapter = SharedPreferencesUtil.getChapter(sharedPreferences,this.getApplicationContext());
        Intent intent = this.getIntent();
        verse = intent.getStringExtra("verse");
        String bookName = intent.getStringExtra("bookName");
        String engName = intent.getStringExtra("bookEng");

        context = this.getApplicationContext();
        srchList = new ArrayList<>();

        srchView = findViewById(R.id.hidbooks);
        favs = findViewById(R.id.hbooks);
        title = findViewById(R.id.hbooktitle);

        //Go Back
        TextView back = findViewById(R.id.hbookBack);
        back.setOnClickListener(v -> {
            Intent data = new Intent(context, HomeActivity.class);
            data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(data);
        });

        //Get all data
        BibleHelper _helper = new BibleHelper(this);
        srchList = _helper.getAllBookmarksByVerse(book,chapter,verse);
        if(srchList==null || srchList.size()==0){
            favs.setText("");
            title.setText("No Bookmarks found for "+ bookName+" "+chapter+":"+verse);
        }else {
            title.setText("Bookmarks containing "+ bookName+" "+chapter+":"+verse);
            if(!bookName.equals(engName)) {
                favs.setText("Bookmarks containing "+ engName+" "+chapter+":"+verse);
            }else{
                favs.setText("");
            }
            srchAdapter = new HomeBookmarkAdapter(HomeBookmarkActivity.this,srchList, bookName, engName,verse);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            // in below two lines we are setting layoutmanager and adapter to our recycler view.
            srchView.setLayoutManager(linearLayoutManager);
            srchView.setAdapter(srchAdapter);
        }
    }
}