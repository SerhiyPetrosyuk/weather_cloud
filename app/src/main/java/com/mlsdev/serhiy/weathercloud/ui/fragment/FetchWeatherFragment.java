package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.asynctasks.GetWeatherAsyncTask;
import com.mlsdev.serhiy.weathercloud.ui.listeners.ForecastListItemListener;

/**
 * Created by android on 27.01.15.
 */
public class FetchWeatherFragment extends BaseFragment {

    private ListView mForecastListView = null;
    private ArrayAdapter<String> mListViewAdapter = null;

    public FetchWeatherFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fetch_weather, container, false);

        finedViews(rootView);
        getWeatherForecast(false);

        return rootView;

    }
    
    private void finedViews(View rootView) {
        mListViewAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.tv_list_item_forecast);
        mForecastListView = (ListView) rootView.findViewById(R.id.lv_forecast);
        mForecastListView.setOnItemClickListener(new ForecastListItemListener(getActivity()));
        mForecastListView.setAdapter(mListViewAdapter);
    }// end finedViews

    public void getWeatherForecast(boolean isUpdateForecast) {
        new GetWeatherAsyncTask(getActivity(), mForecastListView, isUpdateForecast).execute();
    }// end getWeatherForecast
}
