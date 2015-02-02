package com.mlsdev.serhiy.weathercloud.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.fragment.BaseFragment;
import com.mlsdev.serhiy.weathercloud.ui.fragment.FetchWeatherFragment;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(R.id.fragment_holder_in_main_activity) == null) {
            getFragmentManager().beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.fragment_holder_in_main_activity, new FetchWeatherFragment(), FetchWeatherFragment.class.getName())
                    .commit();
        }
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


        getFragmentManager().popBackStackImmediate();
        int fragmentsCount = getFragmentManager().getBackStackEntryCount();


        if (fragmentsCount > 1) {
            String fragmentName = getFragmentManager().getBackStackEntryAt(fragmentsCount-1).getName();
            BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentByTag(fragmentName);
            getSupportActionBar().setTitle(fragment.getFragmentTitle());
        } else if (fragmentsCount == 1) {
            deactivateBackButton();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
