package com.mlsdev.serhiy.weathercloud.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mlsdev.serhiy.weathercloud.internet.ConnectToToUrl;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.util.Cache;
import com.mlsdev.serhiy.weathercloud.util.Constants;
import com.mlsdev.serhiy.weathercloud.util.JsonParser;
import com.mlsdev.serhiy.weathercloud.util.Utility;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 27.01.15.
 */
public class GetWeatherAsyncTask extends AsyncTask<String, Void, Void> {
    
    private Context mContext = null;
    private ListView mListView = null;
    private boolean mIsUpdate = false;

    public GetWeatherAsyncTask(Context mContext, ListView mListView, boolean isUpdate) {
        this.mContext = mContext;
        this.mListView = mListView;
        this.mIsUpdate = isUpdate;
    }

    @Override
    protected Void doInBackground(String... params) {
        
        if (params.length == 0) { return null; }

        if (!Utility.isNetworkEnabled(mContext)) {
            return null;
        }
        
        String location = params[0];
        JsonParser jsonParser = new JsonParser(mContext);
        List<String> forecast = new ArrayList<>();
        
        String json = Cache.getJsonForecast(mContext, location);
        
        try {
            if (json == null || mIsUpdate) {
                Log.d(Constants.LOG_TAG, "Get a forecast from API.");
                HttpURLConnection httpURLConnection = ConnectToToUrl.getHttpURLConnection(UrlBuilder.getUrlString(location));
                json = IOUtils.toString(httpURLConnection.getInputStream());
                Cache.saveJsonForecast(mContext, json, location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }// try-catch
        
        forecast.addAll(jsonParser.getWeatherForecastFromJson(json));
        
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!Utility.isNetworkEnabled(mContext)) {
            Toast.makeText(mContext, "Check the internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
