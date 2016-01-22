package com.mlsdev.serhiy.weathercloud.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.Log;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.data.WeatherContract;
import com.mlsdev.serhiy.weathercloud.internet.ConnectToUrl;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.util.Cache;
import com.mlsdev.serhiy.weathercloud.util.Constants;
import com.mlsdev.serhiy.weathercloud.util.JsonParser;
import com.mlsdev.serhiy.weathercloud.util.Utility;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;

/**
 * Created by android on 12.02.15.
 */
public class ForecastSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

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
            HttpURLConnection httpURLConnection = ConnectToUrl.getHttpURLConnection(UrlBuilder.getUrlString(location));
            String json = IOUtils.toString(httpURLConnection.getInputStream());
            Cache.saveJsonForecast(getContext(), json, location);
            new JsonParser(getContext()).getWeatherForecastFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }// try-catch
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getAccount(context), WeatherContract.CONTENT_AUTHORITY, bundle);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Log.i(Constants.LOG_TAG, "*** configurePeriodicSync ***");

        Account account = getAccount(context);
        String authority = WeatherContract.CONTENT_AUTHORITY;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            SyncRequest.Builder builder = new SyncRequest.Builder();
            SyncRequest syncRequest = builder.syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .build();
            ContentResolver.requestSync(syncRequest);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */

    public static Account getAccount(Context context) {

        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account account = new Account("forecast_sync_adapter", context.getString(R.string.sync_account_type));

        if (accountManager.getPassword(account) == null) {
            if (!accountManager.addAccountExplicitly(account, "", null)) {
                return null;
            }

            Log.i(Constants.LOG_TAG, "*** Account was created ***");
            onAccountCreated(account, context);
        }

        return account;
    }

    private static void onAccountCreated(Account account, Context context) {
        configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(account, WeatherContract.CONTENT_AUTHORITY, true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getAccount(context);
    }

    private static void setLocationStatus(Context context, @LocationStatus int locationStatus) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(context.getString(R.string.pref_location_status_key), locationStatus).commit();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOCATION_STATUS_OK, LOCATION_STATUS_SERVER_DOWN, LOCATION_STATUS_SERVER_INVALID, LOCATION_STATUS_SERVER_UNKNOWN})
    public @interface LocationStatus {
    }

    public static final int LOCATION_STATUS_OK = 0;
    public static final int LOCATION_STATUS_SERVER_DOWN = 1;
    public static final int LOCATION_STATUS_SERVER_INVALID = 2;
    public static final int LOCATION_STATUS_SERVER_UNKNOWN = 3;
}
