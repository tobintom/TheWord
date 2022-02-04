package com.theword.thedigitalword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.theword.thedigitalword.adapter.CardArrayAdapter;
import com.theword.thedigitalword.model.Card;
import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.service.TheWordMetaService;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class LanguageSelectorActivity extends AppCompatActivity {

    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        Util.onActivityCreateSetTheme(this,SharedPreferencesUtil.getTheme(sharedPreferences,this));
        super.onCreate(savedInstanceState);
        Context context = this.getApplicationContext();
        setContentView(R.layout.activity_language_selector);
        listView = (ListView) findViewById(R.id.card_listView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.lang_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tv = (TextView)findViewById(R.id.toollangname);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardArrayAdapter = new CardArrayAdapter(this, R.layout.list_item_card);
        new Thread(){
            @Override
            public void run() {

                try {
                    String jsonData = TheWordMetaService.getLanguages(getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE),
                            context);
                    if(jsonData!=null && jsonData.trim().length()>0) {
                        JSONObject obj = new JSONObject(jsonData);
                        JSONArray arr = obj.getJSONArray("bibles");
                        for(int i=0;i<arr.length();i++){
                            JSONObject o = (JSONObject) arr.get(i);
                            String code = o.getString("id");
                            String name = o.getString("language");
                            String direction = o.getString("direction");
                            ApplicationData.addLanguage(code,name);
                            ApplicationData.addDirection(code,direction);
                            Card card = new Card(name);
                            cardArrayAdapter.add(card);
                        }
                    }
                }catch (Exception e){

                }
                //Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(cardArrayAdapter);
                    }
                });
            }

        }.start();


        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            String lang = cardArrayAdapter.getItem(position).getLine1();
            String code = ApplicationData.getLanguageCode(lang);
            String extLang = SharedPreferencesUtil.getLanguage(sharedPreferences,this.getApplicationContext());
            SharedPreferencesUtil.setLanguage(sharedPreferences,this.getApplicationContext(),code);
            //When language changes reset the widgets updated dates to allow refresh
            if(extLang!=null && code!=null && !extLang.trim().equalsIgnoreCase(code.trim())){
                SharedPreferencesUtil.saveWidgetServiceUpdate(sharedPreferences,this.getApplicationContext(),0);
                SharedPreferencesUtil.saveWidgetSmallServiceUpdate(sharedPreferences,this.getApplicationContext(),0);
            }
            SharedPreferencesUtil.setDirection(sharedPreferences,this.getApplicationContext(),ApplicationData.getDirection(code));
            Intent i = new Intent(this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }
}