package com.mlsdev.serhiy.weathercloud;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.mlsdev.serhiy.weathercloud.data.WeatherContract;
import com.mlsdev.serhiy.weathercloud.data.WeatherDbHelper;

import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.*;

/**
 * Created by android on 02.02.15.
 */
public class TestDb  extends AndroidTestCase{

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Exception {
        mContext.deleteDatabase(WeatherEntry.TABLE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
}
