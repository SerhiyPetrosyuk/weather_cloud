package com.mlsdev.serhiy.weathercloud;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

    public void deleteAllRecords(){
        mContext.getContentResolver().delete(LocationEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(WeatherEntry.CONTENT_URI, null, null);
        
        Cursor cursorLocation = mContext.getContentResolver().query(LocationEntry.CONTENT_URI,null,null,null,null);
        assertEquals(0, cursorLocation.getCount());
        cursorLocation.close();

        Cursor cursorWeather = mContext.getContentResolver().query(WeatherEntry.CONTENT_URI,null,null,null,null);
        assertEquals(0, cursorWeather.getCount());
        cursorLocation.close();
    }
    
    public void setUp(){
        deleteAllRecords();
    }
    
    public void testWriteReadDb(){
        ContentValues contentValues = TestDb.createLocationValues();
        Uri insertedLocationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, contentValues);
        long insertedRowId = ContentUris.parseId(insertedLocationUri);
                
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
        Uri insertedWeatherUri = mContext.getContentResolver().insert(WeatherEntry.CONTENT_URI, weatherValues);
        long insertedWeatherRowId = ContentUris.parseId(insertedWeatherUri);
        
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
        
    }// end testWriteReadDb

    public void testUpdate(){
        deleteAllRecords();
        
        ContentValues locationValues = TestDb.createLocationValues();
        Uri insertedLocationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, locationValues);
        long insertedLocationId = ContentUris.parseId(insertedLocationUri);
        assertTrue(insertedLocationId != -1);
        
        ContentValues valuesToUpdate = new ContentValues(locationValues);
        valuesToUpdate.put(LocationEntry._ID, insertedLocationId);
        valuesToUpdate.put(LocationEntry.COLUMN_CITY_NAME, "Vinnytsya");
        
        int updatedRowsLocation = mContext.getContentResolver().update(
                LocationEntry.CONTENT_URI,
                valuesToUpdate,
                LocationEntry._ID + " = ? ",
                new String[]{Long.toString(insertedLocationId)}
            );
        
        assertEquals(updatedRowsLocation, 1);
        
        Cursor updatedLocationCursor = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(insertedLocationId),
                null,
                null,
                null,
                null
            );
        
        TestDb.validateCursor(updatedLocationCursor, valuesToUpdate);
    }
    
    public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }
    
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
