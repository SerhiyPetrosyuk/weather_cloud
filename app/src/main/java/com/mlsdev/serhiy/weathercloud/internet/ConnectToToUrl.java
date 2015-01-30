package com.mlsdev.serhiy.weathercloud.internet;

import android.util.Log;

import com.mlsdev.serhiy.weathercloud.util.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by android on 27.01.15.
 */
public class ConnectToToUrl {
    
    public static HttpURLConnection getHttpURLConnection(String urlString) {
        HttpURLConnection connection;
        
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(Constants.LOG_TAG, "Can't create URL");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(Constants.LOG_TAG, "Can't open connection");
        }
        
        return null;

    }
    
    public static void closeConnection(HttpURLConnection connectionToClose) {
        
        if (connectionToClose != null)
            connectionToClose.disconnect();

    }
    
}
