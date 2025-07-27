package com.realeyez.trademart.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class DimensionsUtil {

    public static float getScreenDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }
    
}
