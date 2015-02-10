package com.mlsdev.serhiy.weathercloud.asynctasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.mlsdev.serhiy.weathercloud.R;
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
 * Created by android on 31.01.15.
 */
public class UpdateWeatherAsyncTask extends AsyncTask<String, Void, Void>{
    
    private Context mContext = null;
    
    public UpdateWeatherAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) { return null; }
            
//        if (!Utility.isNetworkEnabled(mContext)) {
//            return null;
//        }

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
        
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
//        if (!Utility.isNetworkEnabled(mContext)) {
//            Toast.makeText(mContext, "Check the internet connection", Toast.LENGTH_SHORT).show();
//        }
    }
}
