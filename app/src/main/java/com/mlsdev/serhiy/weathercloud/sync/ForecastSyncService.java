package com.mlsdev.serhiy.weathercloud.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by android on 12.02.15.
 */
public class ForecastSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static ForecastSyncAdapter sForecastSyncAdapter = null;
    
    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock){
            if (sForecastSyncAdapter == null){
                sForecastSyncAdapter = new ForecastSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sForecastSyncAdapter.getSyncAdapterBinder();
    }
}
