package com.mlsdev.serhiy.weathercloud.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.fragment.BaseFragment;
import com.mlsdev.serhiy.weathercloud.ui.fragment.FetchWeatherFragment;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setActionBarIcon(R.drawable.ic_launcher);
        
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder_in_main_activity, new FetchWeatherFragment(), FetchWeatherFragment.class.getName())
                    .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        getSupportFragmentManager().popBackStack();
        int fragmentsCount = getSupportFragmentManager().getBackStackEntryCount();

        if (fragmentsCount == 1)
            deactivateBackButton();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings :
                return true;
            case R.id.update_forecast :
                BaseFragment fragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(FetchWeatherFragment.class.getName());
                fragment.getWeatherForecast(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
