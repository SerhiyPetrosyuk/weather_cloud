package com.mlsdev.serhiy.weathercloud.internet;

import android.net.Uri;

/**
 * Created by android on 27.01.15.
 */
public class UrlBuilder {

    private static final String BASE_DAILY_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";

    public static final String QUERY_Q       = "q";
    public static final String QUERY_MODE     = "mode";
    public static final String QUERY_UNITS    = "units";
    public static final String QUERY_CNT_DAYS = "cnt";

    public static final String Q        = "";
    public static final String MODE     = "json";
    public static final String UNITS    = "metric";
    public static final String CNT_DAYS = "10";
    
    public static String getUrlString(String cityName) {
        Uri uriBuilder = Uri.parse(BASE_DAILY_URL).buildUpon()
                .appendQueryParameter(QUERY_Q, cityName)
                .appendQueryParameter(QUERY_MODE, MODE)
                .appendQueryParameter(QUERY_UNITS, UNITS)
                .appendQueryParameter(QUERY_CNT_DAYS, CNT_DAYS)
                .build();
        
        return uriBuilder.toString();
        
    }
    
}
