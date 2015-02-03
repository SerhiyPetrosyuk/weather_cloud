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
        mContext.deleteDatabase(WeatherEntry.TABLE_NAME);
    }
    
    public void testWriteReadDb(){
        // Test data we're going to insert into the DB to see if it works.
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        contentValues.put(LocationEntry.COLUMN_CITY_NAME, testCityName);
        contentValues.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        contentValues.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);
        
        long insertedRowId = db.insert(
                        LocationEntry.TABLE_NAME,
                        null,
                        contentValues
                    );
        
        // Verify we got a row back.
        assertTrue(insertedRowId != -1);
        Log.d(Constants.LOG_TAG, "New row id is " + insertedRowId);

        // Specify which columns you want.
        String[] columns = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCATION_SETTING,
                LocationEntry.COLUMN_CITY_NAME,
                LocationEntry.COLUMN_COORD_LAT,
                LocationEntry.COLUMN_COORD_LONG
            };

        Cursor cursor = db.query(LocationEntry.TABLE_NAME, columns, null, null, null, null, null);

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()){
            int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            int nameIndex     = cursor.getColumnIndex(LocationEntry.COLUMN_CITY_NAME);
            int latIndex      = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LAT);
            int londIndex     = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LONG);
            
            String location = cursor.getString(locationIndex);
            String name     = cursor.getString(nameIndex);
            Double lat      = cursor.getDouble(latIndex);
            Double lon      = cursor.getDouble(londIndex);


            // Hooray, data was returned!  Assert that it's the right data, and that the database
            // creation code is working as intended.
            // Then take a break.  We both know that wasn't easy.
            assertEquals(testLocationSetting, location);
            assertEquals(testCityName, name);
            assertEquals(testLatitude, lat);
            assertEquals(testLongitude, lon);

            cursor.close();
        } else {
            // That's weird, it works on MY machine...
            fail("No values returned :(");
        }// end if

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
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, insertedRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, testDateText);
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, testDegrees);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, testHuminity);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, testPressure);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, testMaxTemp);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, testMinTemp);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, testShortDesc);
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, testWindSpeed);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, testWeatherId);
        
        long weatherInsertedColumnId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
        
        assertTrue(weatherInsertedColumnId != -1);
        
        String[] columnsWeather = {
                WeatherEntry.COLUMN_LOC_KEY,
                WeatherEntry.COLUMN_DATETEXT,
                WeatherEntry.COLUMN_DEGREES,
                WeatherEntry.COLUMN_HUMIDITY,
                WeatherEntry.COLUMN_PRESSURE,
                WeatherEntry.COLUMN_MAX_TEMP,
                WeatherEntry.COLUMN_MIN_TEMP,
                WeatherEntry.COLUMN_SHORT_DESC,
                WeatherEntry.COLUMN_WIND_SPEED,
                WeatherEntry.COLUMN_WEATHER_ID,
            };
        
        Cursor cursorWeather = db.query(WeatherEntry.TABLE_NAME, columnsWeather, null, null, null, null, null, null);
        
        if (cursorWeather.moveToFirst()){
            int dateextIndex    = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_DATETEXT);
            int degreesIndex    = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_DEGREES);
            int humidityIndex   = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY);
            int pressureIndex   = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_PRESSURE);
            int maxTempIndex    = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP);
            int minTempIndex    = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP);
            int shortDescIndex  = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC);
            int windSpeedIndex  = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED);
            int weatherIdIndex  = cursorWeather.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID);
            
            String  dateText    = cursorWeather.getString(dateextIndex);
            Double  degrees     = cursorWeather.getDouble(degreesIndex);
            Double  humidity    = cursorWeather.getDouble(humidityIndex);
            Double  pressure    = cursorWeather.getDouble(pressureIndex);
            Integer  maxTemp    = cursorWeather.getInt(maxTempIndex);
            Integer  minTemp    = cursorWeather.getInt(minTempIndex);
            String  shortDesc   = cursorWeather.getString(shortDescIndex);
            Double  windSpeed   = cursorWeather.getDouble(windSpeedIndex);
            Integer weatherId   = cursorWeather.getInt(weatherIdIndex);
            
            assertEquals(testDateText, dateText);
            assertEquals(testDegrees, degrees);
            assertEquals(testHuminity, humidity);
            assertEquals(testPressure, pressure);
            assertEquals(testMaxTemp, maxTemp);
            assertEquals(testMinTemp, minTemp);
            assertEquals(testShortDesc, shortDesc);
            assertEquals(testWindSpeed, windSpeed);
            assertEquals(testWeatherId, weatherId);

            cursorWeather.close();
        } else {
            // That's weird, it works on MY machine...
            fail("No values returned :(");
        }// end if
        
        db.close();
    }// end testWriteReadDb
}
