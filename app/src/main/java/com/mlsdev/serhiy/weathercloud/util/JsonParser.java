package com.mlsdev.serhiy.weathercloud.util;

import android.content.Context;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.internet.ConnectToToUrl;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by android on 28.01.15.
 */
public class JsonParser {

    private Context mContext = null;
    private String mDegreeSign = null;
    
    private static final String OWM_LIST        = "list";
    private static final String OWM_WEATHER     = "weather";
    private static final String OWM_TEMPERATURE = "temp";
    private static final String OWM_MAX         = "max";
    private static final String OWM_MIN         = "min";
    private static final String OWM_DATETIME    = "dt";
    private static final String OWM_DESCRIPTION = "main";

    public JsonParser(Context mContext) {
        this.mContext = mContext;
        mDegreeSign = mContext.getString(R.string.degree_sign);
    }

    private String getReadableDate(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM d");
        return dateFormat.format(date);
    }
    
    private String getMinMaxTemp(double min, double max) {
        return Math.round(min) + mDegreeSign + "/" + Math.round(max) + mDegreeSign;
    }// end getMinMaxTemp
    
    public List<String> getWeatherForecastFromJson(String json) {
        int countDays = Integer.parseInt(UrlBuilder.CNT_DAYS);
        List<String> forecast = new ArrayList<>();
        
        try {
            JSONObject rootJsonObject = new JSONObject(json);
            JSONArray jsonArrayForecast = rootJsonObject.getJSONArray(OWM_LIST);
            
            for (int i = 0; i < countDays; i++) {
                String date         = null;
                String description  = null;
                String minMaxTemp   = null;
                
                JSONObject jsonObjectDay = jsonArrayForecast.getJSONObject(i);
                date = getReadableDate(jsonObjectDay.getLong(OWM_DATETIME));
                double minTemp = jsonObjectDay.getJSONObject(OWM_TEMPERATURE).getDouble(OWM_MIN);
                double maxTemp = jsonObjectDay.getJSONObject(OWM_TEMPERATURE).getDouble(OWM_MAX);
                description = jsonObjectDay.getJSONArray(OWM_WEATHER).getJSONObject(0).getString(OWM_DESCRIPTION);
                minMaxTemp = getMinMaxTemp(minTemp, maxTemp);

                forecast.add(date + " :: " + description + " :: " + minMaxTemp);
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return forecast;
    }// end getWeatherForecastFromJson
    
}
