package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.asynctasks.GetWeatherAsyncTask;
import com.mlsdev.serhiy.weathercloud.asynctasks.UpdateWeatherAsyncTask;
import com.mlsdev.serhiy.weathercloud.data.WeatherContract;
import com.mlsdev.serhiy.weathercloud.internet.UrlBuilder;
import com.mlsdev.serhiy.weathercloud.models.Forecast;
import com.mlsdev.serhiy.weathercloud.models.Weather;
import com.mlsdev.serhiy.weathercloud.ui.activity.BaseActivity;
import com.mlsdev.serhiy.weathercloud.ui.activity.DetailActivity;
import com.mlsdev.serhiy.weathercloud.ui.activity.MainActivity;
import com.mlsdev.serhiy.weathercloud.ui.activity.SettingsActivity;
import com.mlsdev.serhiy.weathercloud.ui.adapters.ForecastCursorAdapter;
import com.mlsdev.serhiy.weathercloud.util.Constants;
import com.mlsdev.serhiy.weathercloud.util.JsonParser;
import com.mlsdev.serhiy.weathercloud.util.Utility;

import java.util.Date;

import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.*;

/**
 * Created by android on 27.01.15.
 */
public class FetchWeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private BaseActivity activity = null;
    private ListView mForecastListView = null;
    private String mLocation = null;
    private ForecastCursorAdapter mCursorAdapter = null;
    
    private static int FORECAST_LOADER = 0;

    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATETEXT,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_WEATHER_ID,
            LocationEntry.COLUMN_CITY_NAME
    };
    
    public FetchWeatherFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (BaseActivity)getActivity();
        activity.deactivateBackButton();
        activity.setActionBarIcon(R.drawable.ic_launcher);
        mLocation = Utility.getPreferredLocation(activity);
        
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_forecast :
                updateWeatherForecast();
                break;
            case R.id.action_settings :
                startActivity(new Intent(activity, SettingsActivity.class));
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
        String location = Utility.getPreferredLocation(getActivity());

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
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_fetch_weather, container, false);
        finedViews(rootView);
        setRetainInstance(true);
        return rootView;

    }

    private void finedViews(View rootView) {
        mCursorAdapter = new ForecastCursorAdapter(getActivity(), null, 0);
        mForecastListView = (ListView) rootView.findViewById(R.id.lv_forecast);
        mForecastListView.setOnItemClickListener(new ForecastListItemListener());
        mForecastListView.setAdapter(mCursorAdapter);
    }// end finedViews

    private void getWeatherForecast(boolean isUpdating) {
        new GetWeatherAsyncTask(getActivity(), mForecastListView, isUpdating).execute(Utility.getPreferredLocation(getActivity()));
    }// end getWeatherForecast

    private void updateWeatherForecast() {
        new UpdateWeatherAsyncTask(getActivity()).execute(Utility.getPreferredLocation(getActivity()));
    }// end getWeatherForecast

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        
        String startDate = getDbDateString(new Date());
        String sortOrder = WeatherEntry.COLUMN_DATETEXT + " ASC";

        mLocation = Utility.getPreferredLocation(activity);
        
        CursorLoader loader = new CursorLoader(
                getActivity(),
                WeatherEntry.buildWeatherLocationWithStartDate(mLocation, startDate),
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder
            );
        
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    public class ForecastListItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ForecastCursorAdapter cursorAdapter = (ForecastCursorAdapter) parent.getAdapter();
            Cursor cursor = cursorAdapter.getCursor();
            cursor.moveToPosition(position);
            long weatherRowId = cursor.getLong(cursor.getColumnIndex(WeatherEntry._ID));
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            Bundle args = new Bundle();
            args.putLong(Constants.WEATHER_ROW_ID, weatherRowId);
            
            if (!MainActivity.TWO_PANE) {
                intent.putExtra(Constants.DETAIL_WEATHER, args);
                startActivity(intent);
            } else {
                DetailWeatherInfoFragment fragment = new DetailWeatherInfoFragment();
                fragment.setArguments(args);
                getFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_holder, fragment)
                        .commit();
            }
        }
    }
}

