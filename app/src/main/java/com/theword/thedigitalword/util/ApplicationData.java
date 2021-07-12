package com.theword.thedigitalword.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class  ApplicationData {

    private static Map languages = new HashMap();
    private static Map direction = new HashMap();

    public static String getLanguage(String code){
        return (String)languages.get(code);
    }

    public static void addLanguage(String code, String language){
        languages.put(code,language);
    }

    public static String getDirection(String code){
        return (String)direction.get(code);
    }

    public static void addDirection(String code, String directionString){
        direction.put(code,directionString);
    }

    public static String getLanguageCode(String language){
         Iterator iter = languages.keySet().iterator();
        String code = "";
         while(iter.hasNext()){
             code = (String)iter.next();
             if(languages.get(code).equals(language)){
                 return code;
             }
         }
         return code;
    }
}
