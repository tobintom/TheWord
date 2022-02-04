package com.theword.thedigitalword.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class  ApplicationData {

    private static Map languages = new HashMap();
    private static Map direction = new HashMap();
    private static Map books = new HashMap();
    private static Map engBooks = new HashMap();

    public static String getLanguage(String code){
        return (String)languages.get(code);
    }

    public static Map getLanguages(){ return languages;}

    public static void addLanguage(String code, String language){
        languages.put(code,language);
    }

    public static void addBook(String code, String book){
        books.put(code,book);
    }
    public static void addEngBook(String code, String book){
        engBooks.put(code,book);
    }

    public static void clearBooks(){
        books.clear();
        engBooks.clear();
    }

    public static String getBook(String code){
        return (String)books.get(code);
    }
    public static String getEngBook(String code){
        return (String)engBooks.get(code);
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
