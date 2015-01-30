package com.mlsdev.serhiy.weathercloud.util;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by android on 30.01.15.
 */
public class Cache {
    
    public static void saveJsonForecast(Context context, String json, String location) {
        File cacheFile = new File(context.getCacheDir(), Constants.CACHE_FILE_NAME + "_" + location);

        if (!cacheFile.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(cacheFile, false);
                fileWriter.write(json);
                fileWriter.close();
                Log.i(Constants.LOG_TAG, "Save JSON into the file");
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "Open the file to write Error!");
                e.printStackTrace();
            }
        }
    }// end saveJsonForecast
    
    public static String getJsonForecast(Context context, String location) {
        String json = null;
        File cacheFile = new File(context.getCacheDir(), Constants.CACHE_FILE_NAME + "_" + location);
        
        if (cacheFile.exists()) {
            try {
                FileReader fileReader = new FileReader(cacheFile);
                json = IOUtils.toString(fileReader);
                Log.i(Constants.LOG_TAG, "Read JSON from the file");
            } catch (FileNotFoundException e) {
                Log.e(Constants.LOG_TAG, "File not found Error!");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }// end if
        
        return json;
    }// end getJsonForecast
    
}
