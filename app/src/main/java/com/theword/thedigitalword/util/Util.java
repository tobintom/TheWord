package com.theword.thedigitalword.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;

import androidx.core.graphics.drawable.DrawableCompat;

import com.theword.thedigitalword.ExceptionActivity;
import com.theword.thedigitalword.R;

import java.util.Random;

public class Util {

    private static String selectedBook;

    public static void setSelectedBook(String book){
        selectedBook=book;
    }

    public static String getSelectedBook(){
        return  selectedBook;
    }

    public static int getBibleBookImage(String book , Context context){
        int resourceId = context.getResources().getIdentifier("b"+book, "drawable",
                context.getPackageName());
        return resourceId;
    }

    public static int getBookOutsideColor(String book){
        int ret = 0;
        int b = Integer.parseInt(book);
        if(b>0 && b<6){
            ret = Color.rgb(178, 91, 110);
        }else
            if(b>5 && b<18){
                ret = Color.rgb(148, 86, 9);
            }else
                if((b>17 && b<23)||b==25){
                    ret = Color.rgb(163, 121, 185);
                }else
                if(b==23 || b==24 || (b>25 && b<40)){
                    ret = Color.rgb(54, 69, 119);
                }else
                    if(b>39 && b<44){
                        ret = Color.rgb(201, 127, 14);
                    }else
                        if(b==44){
                            ret = Color.rgb(213, 60, 0);
                        }else
                            if(b>44 && b<58){
                                ret = Color.rgb(2, 134, 42);
                            }else
                                if(b>57 && b<66){
                                    ret = Color.rgb(8, 164, 167);
                                }else
                                    if(b==66){
                                        ret = Color.rgb(4, 135, 164);
                                    }

        return ret;
    }

    public static int getBookColor(String book){
        int ret = 0;
        int b = Integer.parseInt(book);
        if(b>0 && b<6){
            ret = Color.rgb(249, 205, 212);
        }else
        if(b>5 && b<18){
            ret = Color.rgb(223, 199, 167);
        }else
        if((b>17 && b<23)||b==25){
            ret = Color.rgb(236, 205, 232);
        }else
            if(b==23 || b==24 || (b>25 && b<40)){
                ret = Color.rgb(183, 198, 231);
            }else
            if(b>39 && b<44){
                ret = Color.rgb(255, 210, 43);
            }else
            if(b==44){
                ret = Color.rgb(255, 175, 0);
            }else
            if(b>44 && b<58){
                ret = Color.rgb(158, 233, 146);
            }else
            if(b>57 && b<66){
                ret = Color.rgb(220, 255, 251);
            }else
            if(b==66){
                ret = Color.rgb(184, 226, 242);
            }

        return ret;
    }

    public static int getTodayImage(int timeOfDay , Context context){
        Random rand = new Random();
        int random =  rand.nextInt(3) + 1;
        String prefix = "";
        if(timeOfDay >= 0 && timeOfDay < 12){
           prefix = "m";
        }else if(timeOfDay >= 12 && timeOfDay < 17){
            prefix = "a";
        }else if(timeOfDay >= 17 && timeOfDay < 20){
           prefix="e";
        }else if(timeOfDay >= 20 && timeOfDay < 24){
            prefix = "n";
        }
        int resourceId = context.getResources().getIdentifier(prefix+random, "drawable",
                context.getPackageName());
        return resourceId;
    }

    public static  int getRandomSharedResource(Context context) {
        Random rand = new Random();
        String prefix = "q";
        int random = rand.nextInt((16 - 1) + 1) + 1;

        int resourceId = context.getResources().getIdentifier(prefix+random, "drawable",
                context.getPackageName());
        return resourceId;
    }

    public static  int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static String trimToEmpty(String text){
        String RETURN = "";
        if(text!=null && text.length()>0){
            RETURN = text.trim();
        }
        return RETURN;
    }

    public static String ConvertToRanges(String verses){
        String result="";
        if(verses!=null && verses.length()>0 && verses.contains(",")) {
            String[] arr1 = verses.split(",");
            int[] arr = new int[arr1.length];

            for (int x = 0; x < arr1.length; x++) // Convert string array to integer array
            {
                arr[x] = Integer.parseInt(arr1[x]);
            }

            int start, end;  // track start and end
            end = start = arr[0];
            for (int i = 1; i < arr.length; i++) {
                // as long as entries are consecutive, move end forward
                if (arr[i] == (arr[i - 1] + 1)) {
                    end = arr[i];
                } else {
                    // when no longer consecutive, add group to result
                    // depending on whether start=end (single item) or not
                    if (start == end)
                        result += start + ",";
                    else
                        result += start + "-" + end + ",";
                    start = end = arr[i];
                }
            }
            // handle the final group
            if (start == end)
                result += start;
            else {
                result += start + "-" + end;
            }
        }else{
            result = verses;
        }

        return result;
    }

    public static void onFatalException(Context context){
        Intent i = new Intent(context, ExceptionActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    public static boolean checkConnection(Context context){
        boolean hasConnection = true;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            hasConnection = true;
        }else{
            hasConnection = false;
        }
       return hasConnection;
    }

    public static void onActivityCreateSetTheme(Activity activity, int theme){
        activity.setTheme(theme);
    }
}
