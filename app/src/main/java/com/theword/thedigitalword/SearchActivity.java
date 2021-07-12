package com.theword.thedigitalword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theword.thedigitalword.adapter.SearchAdapter;
import com.theword.thedigitalword.model.SearchModel;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    BottomNavigationView navigationView = null;
    private RecyclerView srchView = null;
    private ArrayList srchList = null;
    Context context = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_search);
        context = this.getApplicationContext();
        srchView = findViewById(R.id.idsrchres);
        srchList = new ArrayList();
        String direction = SharedPreferencesUtil.getDirection(sharedPreferences,this.getApplicationContext());
        int DIR = (direction!=null && direction.trim().equalsIgnoreCase("RTL"))? View.LAYOUT_DIRECTION_RTL :View.LAYOUT_DIRECTION_LTR;
        int TEXTDIR = (direction!=null && direction.trim().equalsIgnoreCase("RTL"))? View.TEXT_DIRECTION_RTL :View.TEXT_DIRECTION_LTR;

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setLayoutDirection(DIR);
        searchView.setTextDirection(TEXTDIR);
        TextView hintText = (TextView)findViewById(R.id.searchHint) ;
        hintText.setVisibility(View.VISIBLE);
        hintText.setText("Search for scripture passages using the book name, chapter and verse. E.g. John 3:16 " +
                "\n" +
                "Verses can also be a range, e.g. John 3:16-20. Multiple passages should be separated by semicolon(;). E.g. John 3:16;Genesis 1:1-5;2 Peter 1:5" +
                "\n" +
                "The book name can be in English or in the language selected in the Read Tab. E.g. If the Read Tab is Spanish, searching for John 3:16 and Juan 3:16 retrieves the same passage.\n" +
                "\n" +
                "Search also by Bible text. Results will show Bible passages that match the entered text. Only the first 50 matches are returned. \n" +
                "If searching by text, the text needs to be in the language selected in the Read Tab.");

        //Search Action
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                srchList = new ArrayList();
                boolean hasSearchResults = false;
                boolean hasPassageResults = false;
                String dir = "";
                String id = "";
                if(query!=null && query.trim().length()>0) {
                    TextView hint = (TextView)findViewById(R.id.searchHint);
                    try {
                        String searchData = TheWordContentService.getSearch(sharedPreferences, context, query);
                        //check if SearchData exists
                        if(searchData!=null && searchData.trim().length()>0) {
                            String bookName = "";
                            String englishName="";
                            String bookNumber="";
                            String chapter="";
                            String chapterText="";
                            hint.setText("Click on the list icon to read the full chapter.");
                            JSONObject searchObject = new JSONObject(searchData);
                            String searchResult = searchObject.getString("status");
                            if(searchResult!=null && searchResult.trim().equalsIgnoreCase("SUCCESS")){
                                hasSearchResults = true;
                                //Parse Search Results
                                dir = searchObject.getString("dir");
                                id = searchObject.getString("id");
                                JSONArray oArray =  searchObject.getJSONArray("passages");
                                for(int i=0;i<oArray.length();i++){
                                    JSONObject ol = (JSONObject) oArray.get(i);
                                    bookName = ol.getString("name");
                                    bookNumber = ol.getString("book");
                                    englishName = ol.getString("english");
                                    //Get all the Chapters
                                    JSONArray chapters = ol.getJSONArray("content");
                                    for(int c=0;c<chapters.length();c++) {
                                    JSONObject content = (JSONObject)chapters.get(c);
                                    chapter = content.getString("chapter");
                                    chapterText = "";
                                    JSONArray verses = content.getJSONArray("verses");
                                    for(int v=0;v<verses.length();v++){
                                        JSONObject verse = (JSONObject)verses.get(v);
                                        chapterText = chapterText + verse.getString("verse") +".  " +verse.getString("text") + System.getProperty("line.separator");
                                    }
                                    //Build Adapter
                                        srchList.add(new SearchModel(query,bookName,bookNumber,englishName,chapter,chapterText,dir,id));
                                    }
                                }
                            }else{
                                hasSearchResults = false;
                            }
                        }
                        if(!hasSearchResults) {
                            //Look for passage results only if there are no search results
                            String passageData = TheWordContentService.getPassages(sharedPreferences, context, query);
                            if(passageData!=null && passageData.trim().length()>0){
                                String bookName = "";
                                String englishName="";
                                String bookNumber="";
                                String chapter="";
                                String chapterText="";
                                hint.setText("Click on the list icon to read the full chapter.");
                                JSONObject passageObject = new JSONObject(passageData);
                                String passageResult = passageObject.getString("status");
                                if(passageResult!=null && passageResult.trim().equalsIgnoreCase("SUCCESS")){
                                    hasPassageResults = true;
                                    //Parse Search Results
                                    dir = passageObject.getString("dir");
                                    id = passageObject.getString("id");
                                    JSONArray oArray =  passageObject.getJSONArray("passages");
                                    for(int i=0;i<oArray.length();i++) {
                                        JSONObject ol = (JSONObject) oArray.get(i);
                                        bookName = ol.getString("name");
                                        bookNumber = ol.getString("book");
                                        englishName = ol.getString("english");
                                        //Get all the Chapters
                                        JSONArray chapters = ol.getJSONArray("content");
                                        for (int c = 0; c < chapters.length(); c++) {
                                            JSONObject content = (JSONObject) chapters.get(c);
                                            chapter = content.getString("chapter");
                                            chapterText = "";
                                            JSONArray verses = content.getJSONArray("verses");
                                            for (int v = 0; v < verses.length(); v++) {
                                                JSONObject verse = (JSONObject) verses.get(v);
                                                chapterText = chapterText + verse.getString("verse") + ".  " + verse.getString("text") + System.getProperty("line.separator");
                                            }
                                            //Build Adapter
                                            srchList.add(new SearchModel(query,bookName, bookNumber, englishName, chapter, chapterText, dir, id));
                                        }
                                    }
                                }else{
                                    hasPassageResults = false;
                                }
                            }
                        }
                        //If there are no results
                        if(!hasPassageResults && !hasSearchResults){
//                            TextView results = (TextView)findViewById(R.id.searchShow);
//                            results.setVisibility(View.VISIBLE);
                            hint.setText("No search results found..");
                            hint.setTextColor(Color.BLACK);
                            hint.setTextSize(17);
                            hint.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                        //Set Adapter
                        SearchAdapter srchAdapter = new SearchAdapter(SearchActivity.this, srchList);
                        // below line is for setting a layout manager for our recycler view.
                        // here we are creating vertical list so we will provide orientation as vertical
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        // in below two lines we are setting layoutmanager and adapter to our recycler view.
                        srchView.setLayoutManager(linearLayoutManager);
                        srchView.setAdapter(srchAdapter);
                    }catch (Exception e){
                        //Handle Exception
                    }

                }
                return false;
            }

        });










        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
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
                    case R.id.action_today:
                        startActivity(new Intent(getApplicationContext(), TodayActivity.class));
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
        navigationView.setSelectedItemId(R.id.action_search);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
        finishAffinity();
    }
}
