package com.mlsdev.serhiy.weathercloud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.mlsdev.serhiy.weathercloud.data.WeatherDbHelper;
import com.mlsdev.serhiy.weathercloud.util.Constants;

import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.LocationEntry;
import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.WeatherEntry;

/**
 * Created by android on 02.02.15.
 */
public class TestProvider extends AndroidTestCase{

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Exception {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }
    
    public void testWriteReadDb(){
        WeatherDbHelper dbHelper = new WeatherDbHelper(getContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        
        ContentValues contentValues = TestDb.createLocationValues();
        long insertedRowId = database.insert(LocationEntry.TABLE_NAME, null, contentValues);
        
        assertTrue(insertedRowId != -1);
        
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
            );
        
        TestDb.validateCursor(cursor, contentValues);

        // get location data by _id
        cursor = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(insertedRowId),
                null,
                null,
                null,
                null
        );
        
        TestDb.validateCursor(cursor, contentValues);
        
        ContentValues weatherValues = TestDb.createWeatherValues(insertedRowId);
        
        long insertedWeatherRowId = database.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(insertedWeatherRowId != -1);
        
        cursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
            );
        
        TestDb.validateCursor(cursor, weatherValues);
        
        weatherValues.putAll(contentValues);
        
        cursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocation(TestDb.TEST_LOCATION_SETTINGS),
                null,
                null,
                null,
                null
            );

        TestDb.validateCursor(cursor, weatherValues);

        cursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(TestDb.TEST_LOCATION_SETTINGS, TestDb.TEST_DATE),
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(cursor, weatherValues);
        
        database.close();
    }// end testWriteReadDb

    public void testGetType() {
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        assertEquals(WeatherEntry.CONTENT_TYPE, type);
        
        String testWeatherLocation = "928673";
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocation(testWeatherLocation));
        
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);
        
        String testDate = "20140204";
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocationWithStartDate(testWeatherLocation, testDate));
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);
        
        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        assertEquals(LocationEntry.CONTENT_TYPE, type);
        
        long testLocationId = 923567;
        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(testLocationId));
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }
}
