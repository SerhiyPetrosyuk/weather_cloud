package com.mlsdev.serhiy.weathercloud.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.fragment.FetchWeatherFragment;
import com.mlsdev.serhiy.weathercloud.util.Constants;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(R.id.fragment_holder_in_main_activity) == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_holder_in_main_activity, new FetchWeatherFragment(), FetchWeatherFragment.class.getName())
                    .addToBackStack(FetchWeatherFragment.class.getName())
                    .commit();
        }
    }
    
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
