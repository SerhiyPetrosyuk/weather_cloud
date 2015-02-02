package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.asynctasks.GetWeatherAsyncTask;
import com.mlsdev.serhiy.weathercloud.asynctasks.UpdateWeatherAsyncTask;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.ui.activity.BaseActivity;
import com.mlsdev.serhiy.weathercloud.ui.listeners.ForecastListItemListener;
import com.mlsdev.serhiy.weathercloud.util.Constants;
import com.mlsdev.serhiy.weathercloud.util.JsonParser;

/**
 * Created by android on 27.01.15.
 */
public class FetchWeatherFragment extends Fragment implements BaseFragment {

    private ListView mForecastListView = null;
    private ArrayAdapter<String> mListViewAdapter = null;

    public FetchWeatherFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity)getActivity()).deactivateBackButton();
        ((BaseActivity)getActivity()).setActionBarIcon(R.drawable.ic_launcher);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_forecast :
                getWeatherForecast(true);
                break;
            case R.id.action_settings :
                getFragmentManager().beginTransaction()
                        .addToBackStack(PrefFragment.class.getName())
                        .replace(R.id.fragment_holder_in_main_activity, new PrefFragment())
                        .commit();
                break;
            case R.id.action_map :
                openPreferredLocationInMap();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void openPreferredLocationInMap() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = preferences.getString(getActivity().getString(R.string.pref_location_key), 
                getActivity().getString(R.string.pref_location_default));

        String lon = preferences.getString(Constants.COORDS + JsonParser.OWM_LON, "0");
        String lat = preferences.getString(Constants.COORDS + JsonParser.OWM_LAT, "0");

        Uri geoLocation = Uri.parse("geo:" + lat + "," + lon + "?").buildUpon()
                .appendQueryParameter(UrlBuilder.QUERY_Q, location)
                .build();

        Log.d(Constants.LOG_TAG, "geoLocation: " + geoLocation.toString());
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        
        if ((intent.resolveActivity(getActivity().getPackageManager())) != null)
            startActivity(intent);
        else
            Log.e(Constants.LOG_TAG, "Can't start an activity with location " + location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fetch_weather, container, false);

        finedViews(rootView);
        getWeatherForecast(false);
        setRetainInstance(true);
        return rootView;

    }

    private void finedViews(View rootView) {
        mListViewAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.tv_list_item_forecast);
        mForecastListView = (ListView) rootView.findViewById(R.id.lv_forecast);
        mForecastListView.setOnItemClickListener(new ForecastListItemListener(getActivity()));
        mForecastListView.setAdapter(mListViewAdapter);
    }// end finedViews

    private void getWeatherForecast(boolean isUpdating) {
        new GetWeatherAsyncTask(getActivity(), mForecastListView, isUpdating).execute();
    }// end getWeatherForecast

    private void updateWeatherForecast() {
        new UpdateWeatherAsyncTask(getActivity()).execute();
    }// end getWeatherForecast

    @Override
    public String getFragmentTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public void Hide() {
        getFragmentManager().beginTransaction().hide(this);
    }

    @Override
    public void Show() {
        getFragmentManager().beginTransaction().show(this);
    }
}
