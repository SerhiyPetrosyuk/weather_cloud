package com.mlsdev.serhiy.weathercloud.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.internet.ConnectToToUrl;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.util.Cache;
import com.mlsdev.serhiy.weathercloud.util.Constants;
import com.mlsdev.serhiy.weathercloud.util.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 27.01.15.
 */
public class GetWeatherAsyncTask extends AsyncTask<Void, Void, List<String>> {
    
    private Context mContext = null;
    private ListView mListView = null;
    private boolean mIsUpdate = false;

    public GetWeatherAsyncTask(Context mContext, ListView mListView, boolean isUpdate) {
        this.mContext = mContext;
        this.mListView = mListView;
        this.mIsUpdate = isUpdate;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        JsonParser jsonParser = new JsonParser(mContext);
        List<String> forecast = new ArrayList<>();
        
        String json = Cache.getJsonForecast(mContext, "Vinnytsya");
        
        try {
            if (json == null || mIsUpdate) {
                Log.d(Constants.LOG_TAG, "Get a forecast from API.");
                HttpURLConnection httpURLConnection = ConnectToToUrl.getHttpURLConnection(UrlBuilder.getUrlString("Vinnytsya"));
                json = IOUtils.toString(httpURLConnection.getInputStream());
                Cache.saveJsonForecast(mContext, json, "Vinnytsya");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }// try-catch
        
        forecast.addAll(jsonParser.getWeatherForecastFromJson(json));
        
        return forecast;
    }

    @Override
    protected void onPostExecute(List<String> forecast) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mListView.getAdapter();
        
        if (mIsUpdate) { adapter.clear(); }
        
        adapter.addAll(forecast);
    }
}
