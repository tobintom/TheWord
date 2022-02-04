package com.theword.thedigitalword;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.widget.TextView;

import com.theword.thedigitalword.service.TheWordContentService;
import com.theword.thedigitalword.service.TheWordMetaService;
import com.theword.thedigitalword.service.TokenService;
import com.theword.thedigitalword.util.ApplicationData;
import com.theword.thedigitalword.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

public class WordApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this.getApplicationContext();

        //your codes here
        new Thread() {
            @Override
            public void run() {
                try {
                    //Set the Tokens
                    TokenService.getAccessToken(getSharedPreferences(getString(R.string.app_id), Context.MODE_PRIVATE),
                            context);
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
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Update UI

            }

        }.start();

    }
}
