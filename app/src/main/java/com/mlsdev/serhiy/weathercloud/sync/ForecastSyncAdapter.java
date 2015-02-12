package com.mlsdev.serhiy.weathercloud.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.data.WeatherContract;
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
 * Created by android on 12.02.15.
 */
public class ForecastSyncAdapter extends AbstractThreadedSyncAdapter {
    public ForecastSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (!Utility.isNetworkEnabled(getContext())) {
            return;
        }

        String location = Utility.getPreferredLocation(getContext());

        try {
            Log.d(Constants.LOG_TAG, "Get a forecast from API.");
            HttpURLConnection httpURLConnection = ConnectToToUrl.getHttpURLConnection(UrlBuilder.getUrlString(location));
            String json = IOUtils.toString(httpURLConnection.getInputStream());
            Cache.saveJsonForecast(getContext(), json, location);
            new JsonParser(getContext()).getWeatherForecastFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }// try-catch
    }
    
    public static void syncImmediately(Context context){
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getAccount(context), WeatherContract.CONTENT_AUTHORITY, bundle);
    }


    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     * */ 
    
    public static Account getAccount(Context context){
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account account = new Account("forecast_sync_adapter", context.getString(R.string.sync_account_type));
        
        if (accountManager.getPassword(account) == null){
            if (!accountManager.addAccountExplicitly(account, "", null)){
                return null;
            }
        }
        
        return account;
    }
}
