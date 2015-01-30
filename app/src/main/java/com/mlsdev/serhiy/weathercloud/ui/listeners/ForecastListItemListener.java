package com.mlsdev.serhiy.weathercloud.ui.listeners;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.activity.BaseActivity;
import com.mlsdev.serhiy.weathercloud.ui.activity.MainActivity;
import com.mlsdev.serhiy.weathercloud.ui.fragment.DetailWeatherInfoFragment;

/**
 * Created by android on 29.01.15.
 */
public class ForecastListItemListener implements AdapterView.OnItemClickListener {
    private FragmentActivity mActivity = null;

    public ForecastListItemListener(FragmentActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mActivity.getSupportFragmentManager().beginTransaction()
                .addToBackStack(DetailWeatherInfoFragment.class.getName())
                .replace(R.id.fragment_holder_in_main_activity, new DetailWeatherInfoFragment())
                .commit();
    }
}
