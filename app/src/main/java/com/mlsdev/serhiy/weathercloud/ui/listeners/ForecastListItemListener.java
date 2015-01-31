package com.mlsdev.serhiy.weathercloud.ui.listeners;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.fragment.DetailWeatherInfoFragment;
import com.mlsdev.serhiy.weathercloud.util.Constants;

/**
 * Created by android on 29.01.15.
 */
public class ForecastListItemListener implements AdapterView.OnItemClickListener {
    private Activity mActivity = null;

    public ForecastListItemListener(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
        String detailInfo = ((ArrayAdapter<String>) parent.getAdapter()).getItem(position);
        Bundle args = new Bundle();
        args.putString(Constants.DETAIL_WEATHER, detailInfo);
        DetailWeatherInfoFragment fragment = new DetailWeatherInfoFragment();
        fragment.setArguments(args);
        
        mActivity.getFragmentManager().beginTransaction()
                .addToBackStack(DetailWeatherInfoFragment.class.getName())
                .replace(R.id.fragment_holder_in_main_activity, fragment,
                        DetailWeatherInfoFragment.class.getName())
                .commit();
    }
}
