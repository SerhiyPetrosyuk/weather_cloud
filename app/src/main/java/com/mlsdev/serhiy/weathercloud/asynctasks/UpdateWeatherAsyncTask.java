package com.mlsdev.serhiy.weathercloud.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.internet.ConnectToToUrl;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.util.Cache;
import com.mlsdev.serhiy.weathercloud.util.Constants;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by android on 31.01.15.
 */
public class UpdateWeatherAsyncTask extends AsyncTask<Void, Void, String>{
    
    private Context mContext = null;
    
    public UpdateWeatherAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String location = preferences.getString(mContext.getString(R.string.pref_location_key), mContext.getString(R.string.pref_location_default));

        try {
            Log.d(Constants.LOG_TAG, "Get a forecast from API.");
            HttpURLConnection httpURLConnection = ConnectToToUrl.getHttpURLConnection(UrlBuilder.getUrlString(location));
            String json = IOUtils.toString(httpURLConnection.getInputStream());
            Cache.saveJsonForecast(mContext, json, location);
        } catch (IOException e) {
            e.printStackTrace();
        }// try-catch
        
        return null;
    }
}
