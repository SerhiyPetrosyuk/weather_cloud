package com.mlsdev.serhiy.weathercloud.ui.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.fragment.DetailWeatherInfoFragment;
import com.mlsdev.serhiy.weathercloud.util.Constants;

/**
 * Created by android on 07.02.15.
 */
public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Fragment fragment = new DetailWeatherInfoFragment();
        fragment.setArguments(getIntent().getBundleExtra(Constants.DETAIL_WEATHER));
        getFragmentManager().beginTransaction()
                .replace(R.id.detail_activity_fragment_holder, fragment)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }
}
