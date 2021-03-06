package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.activity.BaseActivity;
import com.mlsdev.serhiy.weathercloud.ui.activity.MainActivity;
import com.mlsdev.serhiy.weathercloud.ui.activity.SettingsActivity;
import com.mlsdev.serhiy.weathercloud.util.Constants;
import com.mlsdev.serhiy.weathercloud.util.Utility;

import static com.mlsdev.serhiy.weathercloud.data.WeatherContract.*;

/**
 * Created by android on 29.01.15.
 */
public class DetailWeatherInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());
    private TextView mMaxTemp;
    private TextView mMinTemp;
    private TextView mHumidity;
    private TextView mWindSpeed;
    private TextView mPressure;
    private TextView mDesc;
    private ImageView mIcon;
    private String mLocation;
    private LinearLayout mContainer;
    
    private TextView mDayName;
    private TextView mMonthName;
    
    private static int DETAIL_LOADER = 1;

    private long mWeatherRowId;
    
    public DetailWeatherInfoFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!MainActivity.TWO_PANE) {
            final BaseActivity activity = (BaseActivity) getActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setTitle(getString(R.string.detail_fragment));
            activity.activateBackButton();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.fragment_detail_info, container, false);
        findViews(rootView);
        activateViews();
        setRetainInstance(true);
        
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        if (!MainActivity.TWO_PANE) {
            mContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }
        
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }

    private void findViews(View rootView){
        mContainer = (LinearLayout) rootView.findViewById(R.id.ll_container);
        mDayName = (TextView) rootView.findViewById(R.id.tv_day_detail_fragment);
        mMonthName = (TextView) rootView.findViewById(R.id.tv_month_detail_fragment);
        mMaxTemp = (TextView) rootView.findViewById(R.id.tv_max_temp_detail_fragment);
        mMinTemp = (TextView) rootView.findViewById(R.id.tv_min_temp_detail_fragment);
        mHumidity = (TextView) rootView.findViewById(R.id.tv_humidity_detail_fragment);
        mPressure = (TextView) rootView.findViewById(R.id.tv_pressure_detail_fragment);
        mWindSpeed = (TextView) rootView.findViewById(R.id.tv_wind_speed_detail_fragment);
        mDesc = (TextView) rootView.findViewById(R.id.tv_desc_detail_fragment);
        mIcon = (ImageView) rootView.findViewById(R.id.iv_icon_detail_fragment);
    }
    
    private void activateViews(){
        Bundle args = getArguments();
        
        if (args != null){ mWeatherRowId = args.getLong(Constants.WEATHER_ROW_ID); }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider;
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        
        if (shareActionProvider != null)
            shareActionProvider.setShareIntent(createShareIntent());
        else
            Log.e(Constants.LOG_TAG, "Share action provider is null&");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!MainActivity.TWO_PANE) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    break;
                default:
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
    private Intent createShareIntent() {
        String dayName = mDayName.getText().toString();
        String monthName = mMonthName.getText().toString();
        String temp = mMinTemp.getText().toString() + "/" + mMaxTemp.getText().toString();
        String weatherDesc = mDesc.getText().toString();
        
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Weather Cloud");
        intent.putExtra(Intent.EXTRA_TEXT, dayName + " " + monthName + " Temp.: " + temp + " Weather: " + weatherDesc + FORECAST_SHARE_HASHTAG);
        
        return intent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mLocation = Utility.getPreferredLocation(getActivity());
        if (mWeatherRowId == 0){ return null; }

        return new CursorLoader(
                getActivity(),
                WeatherEntry.buildWeatherUri(mWeatherRowId),
                null,
                null,
                null,
                null
            );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()){
            String date = cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_DATETEXT));
            double minTemp = cursor.getDouble(cursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP));
            double maxTemp = cursor.getDouble(cursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP));
            double humidity= cursor.getDouble(cursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY));
            double windSped= cursor.getDouble(cursor.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED));
            double pressure= cursor.getDouble(cursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE));
            String weather = cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC));
            int weatherId = cursor.getInt(cursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID));

            boolean isMetric = Utility.isMetricUnits(getActivity());

            String dayName = Utility.getDayName(getActivity(), date);
            String monthAndDate =  Utility.getFormattedMonthDay(getActivity(), date);
            String minTempStr = Utility.formatTemperature(minTemp, isMetric);
            String maxTempStr = Utility.formatTemperature(maxTemp, isMetric);

            mDayName.setText(dayName);
            mMonthName.setText(monthAndDate);
            mDesc.setText(weather);
            mMaxTemp.setText(maxTempStr + getActivity().getString(R.string.degree_sign));
            mMinTemp.setText(minTempStr + getActivity().getString(R.string.degree_sign));
            mHumidity.setText(Utility.getFormattedHumidity(getActivity(), humidity));
            mPressure.setText(Utility.getFormattedPressure(getActivity(), pressure));
            mWindSpeed.setText(Utility.getFormattedWindSpeed(getActivity(), windSped));
            mIcon.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
    
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int DISTANCE = 100;
        private static final int VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int startX = (int) e1.getX();
            int endX = (int) e2.getX();
            
            int startY = (int)  e1.getY();
            int endY = (int)  e2.getY();
            
            int startYAndEndYDiff = startY - endY;

            boolean isFromLeftToRight = (endX - startX) > DISTANCE;
            boolean isFastEnough = Math.abs(velocityX) > VELOCITY;
            boolean isHorizontalLine = startYAndEndYDiff <= 100 && startYAndEndYDiff >= -100;
            
            if (isFastEnough && isFromLeftToRight && isHorizontalLine){
                getActivity().onBackPressed();
                return true;
            }
            
            return false;
        }
    }
}
