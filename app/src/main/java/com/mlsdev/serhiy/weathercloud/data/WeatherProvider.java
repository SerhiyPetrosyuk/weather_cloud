package com.mlsdev.serhiy.weathercloud.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.*;

/**
 * Created by android on 03.02.15.
 */
public class WeatherProvider extends ContentProvider {

    private static final int WEATHER = 100;
    private static final int WEATHER_WITH_LOCATION = 101;
    private static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    private static final int LOCATION = 300;
    private static final int LOCATION_ID = 301;
    
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private static WeatherDbHelper mDbHelper;
    
    private static final SQLiteQueryBuilder sWeatherByLocationIdQueryBuilder;
    
    static {
        sWeatherByLocationIdQueryBuilder = new SQLiteQueryBuilder();
        sWeatherByLocationIdQueryBuilder.setTables(
                WeatherEntry.TABLE_NAME + " INNER JOIN " + LocationEntry.TABLE_NAME + 
                " ON " + WeatherEntry.TABLE_NAME + "." + WeatherEntry.COLUMN_LOC_KEY +
                " = " + LocationEntry.TABLE_NAME + "." + LocationEntry._ID
            );
    }
    
    private static final String sLocationSettingsSelection = LocationEntry.TABLE_NAME + "." + LocationEntry.COLUMN_LOCATION_SETTING + " = ?";
    private static final String sLocationSettingsWithStartDataSelection = 
            LocationEntry.TABLE_NAME + "." + LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
            WeatherEntry.TABLE_NAME + "." + WeatherEntry.COLUMN_DATETEXT + " >= ? ";
    public static final String sLocationSettingAndDaySelection = 
            LocationEntry.TABLE_NAME + "." + LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " + 
            WeatherEntry.TABLE_NAME + "." + WeatherEntry.COLUMN_DATETEXT + " = ? ";
    public static final String sDeleteLocationRowSelection = LocationEntry.TABLE_NAME + "." + LocationEntry._ID + " = ? ";
    public static final String sDeleteWeatherRowSelection = WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID + " = ? ";

    private static Cursor getWeatherByLocationSettingAndDay(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherEntry.getLocationSettingsFromUri(uri);
        String day = WeatherEntry.getDateFromUri(uri);

        return sWeatherByLocationIdQueryBuilder.query(
                mDbHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelection,
                new String[]{locationSetting, day},
                null,
                null,
                sortOrder
            );
    }
    
    private static Cursor getWeatherByLocationSettings(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherEntry.getLocationSettingsFromUri(uri);
        String startDate = WeatherEntry.getStartDateFromUri(uri);
        
        String[] selectionArgs;
        String selection;
        
        if (startDate != null){
            selection = sLocationSettingsWithStartDataSelection;
            selectionArgs = new String[]{locationSetting, startDate};
        } else {
            selection = sLocationSettingsSelection;
            selectionArgs = new String[]{locationSetting};
        }
        
        Cursor cursor = sWeatherByLocationIdQueryBuilder.query(
                mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            );
        
        return cursor;
    }
    
    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        
        matcher.addURI(authority, PATH_WEATHER, WEATHER);
        matcher.addURI(authority, PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        matcher.addURI(authority, PATH_WEATHER + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);
        
        matcher.addURI(authority, PATH_LOCATION, LOCATION);
        matcher.addURI(authority, PATH_LOCATION + "/#", LOCATION_ID);
        
        return matcher;
    }
    
    @Override
    public boolean onCreate() {
        mDbHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        
        Cursor resultCursor = null;
        
        switch (URI_MATCHER.match(uri)){
            case WEATHER_WITH_LOCATION_AND_DATE :
                resultCursor = getWeatherByLocationSettingAndDay(uri, projection, sortOrder);
                break;
            case WEATHER_WITH_LOCATION :
                resultCursor = getWeatherByLocationSettings(uri, projection, sortOrder);
                break;
            case WEATHER :
                resultCursor = mDbHelper.getReadableDatabase().query(
                        WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOCATION_ID :
                resultCursor = mDbHelper.getReadableDatabase().query(
                        LocationEntry.TABLE_NAME,
                        projection,
                        LocationEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                    );
                break;
            case LOCATION :
                resultCursor = mDbHelper.getReadableDatabase().query(
                        LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                    );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        
        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
        
        return resultCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = URI_MATCHER.match(uri);
        
        switch (match){
            case WEATHER_WITH_LOCATION_AND_DATE :
                return WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION :
                return WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER : 
                return  WeatherEntry.CONTENT_TYPE;
            case LOCATION :
                return LocationEntry.CONTENT_TYPE;
            case LOCATION_ID :
                return LocationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        Uri returnUri = null;
        
        switch (match){
            case WEATHER :
                long insertedId = database.insert(WeatherEntry.TABLE_NAME, null, values);
                if (insertedId > 0){
                    returnUri = WeatherEntry.buildWeatherUri(insertedId);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case LOCATION :
                insertedId = database.insert(LocationEntry.TABLE_NAME, null, values);
                if (insertedId > 0){
                    returnUri = LocationEntry.buildLocationUri(insertedId);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        
        getContext().getContentResolver().notifyChange(returnUri, null);
        database.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int deletedRows = 0;
        
        switch (match){
            case WEATHER :
                deletedRows = database.delete(WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION :
                deletedRows = database.delete(LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        
        if (selection == null || deletedRows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        
        database.close();
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int rowsUpdated = 0;
        
        switch (match){
            case WEATHER :
                rowsUpdated = database.update(WeatherEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LOCATION :
                rowsUpdated = database.update(LocationEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        
        database.close();
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        
        switch (match){
            case WEATHER :
                database.beginTransaction();
                int returnCount = 0;
                
                try {
                    for (ContentValues contentValues : values){
                        long _id = database.insert(WeatherEntry.TABLE_NAME, null, contentValues);
                        
                        if (_id > 0) { returnCount++; }
                    }
                    
                    database.setTransactionSuccessful();
                } finally {
                    database.endTransaction();
                }
                
                getContext().getContentResolver().notifyChange(uri, null);
                return  returnCount;
        }
        
        return super.bulkInsert(uri, values);
    }
}
