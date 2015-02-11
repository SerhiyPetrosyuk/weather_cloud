package com.mlsdev.serhiy.weathercloud.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mlsdev.serhiy.weathercloud.internet.ConnectToToUrl;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.util.Cache;
import com.mlsdev.serhiy.weathercloud.util.Constants;
import com.mlsdev.serhiy.weathercloud.util.JsonParser;
import com.mlsdev.serhiy.weathercloud.util.Utility;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by android on 11.02.15.
 */
public class WeatherService extends IntentService {
    
    private Context mContext;
    
    public WeatherService() {
        super("MyWeatherService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (!Utility.isNetworkEnabled(mContext)) {
            return;
        }

        String location = Utility.getPreferredLocation(mContext);

        try {
            Log.d(Constants.LOG_TAG, "Get a forecast from API.");
            HttpURLConnection httpURLConnection = ConnectToToUrl.getHttpURLConnection(UrlBuilder.getUrlString(location));
            String json = IOUtils.toString(httpURLConnection.getInputStream());
            Cache.saveJsonForecast(mContext, json, location);
            new JsonParser(mContext).getWeatherForecastFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }// try-catch
        
    }

}
