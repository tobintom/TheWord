package com.theword.thedigitalword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.util.SharedPreferencesUtil;
import com.theword.thedigitalword.util.Util;

import org.json.JSONObject;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3500;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE);
        JSONObject jObject = null;
        String text = "";
        String book = "";
        String dir = "ltr";
        try {
            String jsonData = TheWordContentService.getRandomDailyVerse(sharedPreferences, this.getApplicationContext());
            if(jsonData!=null && jsonData.trim().length()>0) {
                jObject = new JSONObject(jsonData);
                dir = jObject.getString("dir");
                JSONObject o = (JSONObject) jObject.getJSONArray("passages").get(0);
                book = o.getString("name");
                //get rest
                JSONObject p = (JSONObject)o.getJSONArray("content").get(0);
                book = book +" "+ p.getString("chapter");
                JSONObject t = (JSONObject) p.getJSONArray("verses").get(0);
                book = book+":"+t.getString("verse");
                text = t.getString("text")+ "\n";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_splash);
        ImageView view= (ImageView) findViewById(R.id.splash_image);
        view.setImageResource(Util.getRandomResource());

         TextView tv =  (TextView) findViewById(R.id.splash_text);
         tv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
         TextView bv =  (TextView) findViewById(R.id.splash_book);
         bv.setTextDirection(dir.equalsIgnoreCase("LTR")?TextView.TEXT_DIRECTION_LTR:TextView.TEXT_DIRECTION_RTL);
         tv.setText(text);
         bv.setText(book);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Run after splash screen is done
                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }


}
