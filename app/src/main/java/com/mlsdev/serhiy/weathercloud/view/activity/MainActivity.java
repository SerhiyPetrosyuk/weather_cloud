package com.mlsdev.serhiy.weathercloud.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mlsdev.serhiy.weathercloud.R;


public class MainActivity extends BaseActivity {

    public static boolean TWO_PANE;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (findViewById(R.id.detail_fragment_holder) != null){
            TWO_PANE = true;
        } else {
            TWO_PANE = false;
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
