package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.ui.activity.BaseActivity;
import com.mlsdev.serhiy.weathercloud.util.Constants;

/**
 * Created by android on 29.01.15.
 */
public class DetailWeatherInfoFragment extends Fragment implements BaseFragment {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView mDetailInfoTextView = null;
    private String mDetailWeather = null;
    
    public DetailWeatherInfoFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseActivity activity = (BaseActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(getString(R.string.detail_fragment));
        activity.activateBackButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_info, container, false);
        findViews(rootView);
        activateViews();
        setRetainInstance(true);
        return rootView;
    }
    
    private void findViews(View rootView){
        mDetailInfoTextView = (TextView) rootView.findViewById(R.id.tv_detail_weather);
    }
    
    private void activateViews(){
        Bundle args = getArguments();
        
        if (args != null){
            mDetailWeather = args.getString(Constants.DETAIL_WEATHER);
            
            if (mDetailWeather != null)
                mDetailInfoTextView.setText(mDetailWeather);
        }
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
        switch (item.getItemId()){
            case R.id.action_settings :
                getFragmentManager().beginTransaction()
                        .addToBackStack(PrefFragment.class.getName())
                        .replace(R.id.fragment_holder_in_main_activity, new PrefFragment(), PrefFragment.class.getName())
                        .commit();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private Intent createShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mDetailWeather + FORECAST_SHARE_HASHTAG);
        
        return intent;
    }

    @Override
    public String getFragmentTitle() {
        return getString(R.string.detail_fragment);
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
