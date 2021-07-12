package com.theword.thedigitalword;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.theword.thedigitalword.service.TheWordMetaService;
import com.theword.thedigitalword.service.TokenService;
import com.theword.thedigitalword.util.ApplicationData;

import org.json.JSONArray;
import org.json.JSONObject;

public class WordApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Set Tokens
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
            //your codes here
            TokenService.getAccessToken(getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE),
                    this.getApplicationContext());
        try {
            String jsonData = TheWordMetaService.getLanguages(getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE),
                    this.getApplicationContext());
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
        }catch (Exception e){

        }

    }
}
