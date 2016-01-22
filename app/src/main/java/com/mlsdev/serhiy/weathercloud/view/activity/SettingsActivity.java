package com.mlsdev.serhiy.weathercloud.view.activity;

import android.os.Bundle;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.view.fragment.PrefFragment;

/**
 * Created by android on 07.02.15.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_fragment_holder, new PrefFragment())
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }
}
