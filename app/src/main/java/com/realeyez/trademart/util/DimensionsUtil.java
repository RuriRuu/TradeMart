package com.realeyez.trademart.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class DimensionsUtil {

    public static int getScreenDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(metrics.density);
    }
    
}
