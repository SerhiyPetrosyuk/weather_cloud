package com.mlsdev.serhiy.weathercloud.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.fragment.StockFragment;

/**
 * Created by android on 28.01.15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        mToolbar = (Toolbar) findViewById(R.id.app_toolbar);

        if (mToolbar != null){
            setSupportActionBar(mToolbar);
        }

    }

    protected abstract int getLayoutResource();

    public void setActionBarIcon(int iconRes) {
        mToolbar.setNavigationIcon(iconRes);
    }

    /**
     * @param title The title of the current activity
     * */
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
    
    public void activateBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void deactivateBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        setActionBarIcon(R.drawable.ic_launcher);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
