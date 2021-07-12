package com.theword.thedigitalword.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

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

    public static  int getRandomResource() {
        Random rand = new Random();
        int random =  rand.nextInt((16 - 1) + 1) + 1;
        int returnResource = R.drawable.a;
        switch (random){
            case 1:
                returnResource = R.drawable.a;
                break;
            case 2:
                returnResource = R.drawable.b;
                break;
            case 3:
                returnResource = R.drawable.c;
                break;
            case 4:
                returnResource = R.drawable.d;
                break;
            case 5:
                returnResource = R.drawable.e;
                break;
            case 6:
                returnResource = R.drawable.f;
                break;
            case 7:
                returnResource = R.drawable.g;
                break;
            case 8:
                returnResource = R.drawable.h;
                break;
            case 9:
                returnResource = R.drawable.i;
                break;
            case 10:
                returnResource = R.drawable.j;
                break;
            case 11:
                returnResource = R.drawable.k;
                break;
            case 12:
                returnResource = R.drawable.l;
                break;
            case 13:
                returnResource = R.drawable.m;
                break;
            case 14:
                returnResource = R.drawable.n;
                break;
            case 15:
                returnResource = R.drawable.o;
                break;
            case 16:
                returnResource = R.drawable.p;
                break;
            default:
                returnResource = R.drawable.a;
                break;
        }
        return returnResource;
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
}
