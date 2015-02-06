package com.mlsdev.serhiy.weathercloud.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mlsdev.serhiy.weathercloud.R;

/**
 * Created by android on 05.02.15.
 */
public class Utility {
    
    public static String getPreferredLocation(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_location_key), 
                context.getString(R.string.pref_location_default));
    }
    
}
