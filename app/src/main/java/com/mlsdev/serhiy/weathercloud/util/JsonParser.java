package com.mlsdev.serhiy.weathercloud.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.data.WeatherContract;
import com.mlsdev.serhiy.weathercloud.internet.ConnectToToUrl;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.models.City;
import com.mlsdev.serhiy.weathercloud.models.Forecast;
import com.mlsdev.serhiy.weathercloud.models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.*;

/**
 * Created by android on 28.01.15.
 */
public class JsonParser {

    private Context mContext = null;
    private String mDegreeSign = null;

    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    public static boolean DEBUG = false;
    public static final String OWM_LON         = "lon";
    public static final String OWM_LAT         = "lat";

    private Forecast mForecast = null;
    private List<ContentValues> mValuesList = null;
    
    public JsonParser(Context mContext) {
        this.mContext = mContext;
        mDegreeSign = mContext.getString(R.string.degree_sign);
    }

    private void saveCoord(){
            double lon = mForecast.getCity().getCoord().getLon();
            double lat = mForecast.getCity().getCoord().getLat();
            
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.COORDS + OWM_LON, Double.toString(lon));
            editor.putString(Constants.COORDS + OWM_LAT, Double.toString(lat));
    }
    
    public List<String> getWeatherForecastFromJson(String json) {
        List<String> forecast = new ArrayList<>();

        mForecast = new Gson().fromJson(json, Forecast.class);
        List<com.mlsdev.serhiy.weathercloud.models.List> daysList = mForecast.getList();
        mValuesList = new ArrayList<>(daysList.size());
        saveCoord();
        
        long locationId = addLocation(mForecast.getCity());
        
        for (com.mlsdev.serhiy.weathercloud.models.List day : daysList){
            addContentValuesToVector(locationId, day);
        }
            
        // If mValuesList.size() > 0 start multi insert
        if (mValuesList.size() > 0){
            ContentValues[] valuesArray = new ContentValues[mValuesList.size()];
            int rowsInserted = mContext.getContentResolver().bulkInsert(
                    WeatherEntry.CONTENT_URI,
                    mValuesList.toArray(valuesArray)
                );
            Log.v(Constants.LOG_TAG, "inserted " + rowsInserted + " rows of weather data");
            
            if (DEBUG){
                Cursor cursor = mContext.getContentResolver().query(
                        WeatherEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    );
                
                try {
                    if (cursor.moveToFirst()) {
                        ContentValues contentValues = new ContentValues();
                        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

                        Log.v(Constants.LOG_TAG, "Query succeeded! **********");
                        for (String key : contentValues.keySet()) {
                            Log.v(Constants.LOG_TAG, key + ": " + contentValues.getAsString(key));
                        }
                    } else {
                        Log.v(Constants.LOG_TAG, "Query failed! :( **********");
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        }
        
        return forecast;
    }// end getWeatherForecastFromJson
    
    public long addLocation(City city){
        
        // First, check if this location already exists
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                new String[]{LocationEntry._ID},
                LocationEntry.COLUMN_LOCATION_SETTING + " = ? ",
                new String[]{city.getId().toString()},
                null
        );
        
        if (cursor.moveToFirst()){
            return cursor.getLong(cursor.getColumnIndex(LocationEntry._ID));
        } else {
            ContentValues values = new ContentValues();
            values.put(LocationEntry.COLUMN_LOCATION_SETTING, city.getId().toString());
            values.put(LocationEntry.COLUMN_CITY_NAME, city.getName());
            values.put(LocationEntry.COLUMN_COORD_LONG, city.getCoord().getLon());
            values.put(LocationEntry.COLUMN_COORD_LAT, city.getCoord().getLat());
            
            Uri insertedUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, values);
            return ContentUris.parseId(insertedUri);
        }
        
    }
    
    private void addContentValuesToVector(long locationId, com.mlsdev.serhiy.weathercloud.models.List day){
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherEntry.COLUMN_LOC_KEY, locationId);
        contentValues.put(WeatherEntry.COLUMN_DATETEXT, WeatherContract.getDbDateString(new Date(day.getDt() * 1000L)));
        contentValues.put(WeatherEntry.COLUMN_DEGREES, day.getDeg());
        contentValues.put(WeatherEntry.COLUMN_HUMIDITY, day.getHumidity());
        contentValues.put(WeatherEntry.COLUMN_PRESSURE, day.getPressure());
        contentValues.put(WeatherEntry.COLUMN_WIND_SPEED, day.getSpeed());
        contentValues.put(WeatherEntry.COLUMN_WEATHER_ID, day.getWeather().get(0).getId());
        contentValues.put(WeatherEntry.COLUMN_SHORT_DESC, day.getWeather().get(0).getDescription());
        contentValues.put(WeatherEntry.COLUMN_MAX_TEMP, day.getTemp().getMax());
        contentValues.put(WeatherEntry.COLUMN_MIN_TEMP, day.getTemp().getMin());
        mValuesList.add(contentValues);
    }
}
