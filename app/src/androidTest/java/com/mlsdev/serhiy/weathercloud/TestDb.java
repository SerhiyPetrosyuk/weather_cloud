package com.mlsdev.serhiy.weathercloud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.mlsdev.serhiy.weathercloud.data.WeatherContract;
import com.mlsdev.serhiy.weathercloud.data.WeatherDbHelper;
import com.mlsdev.serhiy.weathercloud.util.Constants;

import java.util.Map;
import java.util.Set;

import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.*;

/**
 * Created by android on 02.02.15.
 */
public class TestDb  extends AndroidTestCase{

    public void testCreateDb() throws Exception {
        mContext.deleteDatabase(WeatherEntry.TABLE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
    
    public void testWriteReadDb(){
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues contentValues = createLocationValues();
        long insertedRowId = db.insert(LocationEntry.TABLE_NAME, null, contentValues);
        
        // Verify we got a row back.
        assertTrue(insertedRowId != -1);
        Log.d(Constants.LOG_TAG, "New row id is " + insertedRowId);
        Cursor cursor = db.query(LocationEntry.TABLE_NAME, null, null, null, null, null, null);
        validateCursor(cursor, contentValues);
        
        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = createWeatherValues(insertedRowId);
        long weatherInsertedColumnId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);

        // Verify we got a row back.
        assertTrue(weatherInsertedColumnId != -1);
        Log.d(Constants.LOG_TAG, "New row id is " + insertedRowId);
        Cursor cursorWeather = db.query(WeatherEntry.TABLE_NAME, null, null, null, null, null, null, null);
        validateCursor(cursorWeather, weatherValues);
        
        db.close();
    }// end testWriteReadDb
    
    static void validateCursor(Cursor cursor, ContentValues expectedValues) {
       
        assertTrue(cursor.moveToFirst());
        
        Set<Map.Entry<String, Object>> entrySet = expectedValues.valueSet();
        
        for (Map.Entry<String, Object> entry : entrySet){
            String columnName = entry.getKey();
            Integer columnIndex = cursor.getColumnIndex(columnName);
            assertFalse(columnIndex == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, cursor.getString(columnIndex));
        }
        
        cursor.close();
    }
    
    static ContentValues createLocationValues() {
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        ContentValues contentValues = new ContentValues();
        contentValues.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        contentValues.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        contentValues.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        contentValues.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);
        
        return contentValues;
    }
    
    static ContentValues createWeatherValues(long insertedRowId) {
        String testDateText     = "20141205";
        Double testDegrees      = 1.1;
        Double testHuminity     = 1.2;
        Double testPressure     = 1.3;
        Integer testMaxTemp      = 75;
        Integer testMinTemp      = 65;
        String testShortDesc    = "Asteroids";
        Double testWindSpeed    = 5.5;
        Integer testWeatherId   = 321;

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherEntry.COLUMN_LOC_KEY, insertedRowId);
        contentValues.put(WeatherEntry.COLUMN_DATETEXT, testDateText);
        contentValues.put(WeatherEntry.COLUMN_DEGREES, testDegrees);
        contentValues.put(WeatherEntry.COLUMN_HUMIDITY, testHuminity);
        contentValues.put(WeatherEntry.COLUMN_PRESSURE, testPressure);
        contentValues.put(WeatherEntry.COLUMN_MAX_TEMP, testMaxTemp);
        contentValues.put(WeatherEntry.COLUMN_MIN_TEMP, testMinTemp);
        contentValues.put(WeatherEntry.COLUMN_SHORT_DESC, testShortDesc);
        contentValues.put(WeatherEntry.COLUMN_WIND_SPEED, testWindSpeed);
        contentValues.put(WeatherEntry.COLUMN_WEATHER_ID, testWeatherId);
        
        return contentValues;
    }
}
