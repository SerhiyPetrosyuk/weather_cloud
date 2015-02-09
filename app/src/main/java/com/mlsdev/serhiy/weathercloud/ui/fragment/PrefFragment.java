package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.asynctasks.UpdateWeatherAsyncTask;
import com.mlsdev.serhiy.weathercloud.ui.activity.BaseActivity;

/**
 * Created by android on 30.01.15.
 */
public class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, StockFragment{

    private boolean mIsGetDefaultValues = true;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_screen);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_units_key)));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity activity = (BaseActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(getString(R.string.action_settings));
        activity.activateBackButton();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        
        String prefValue = (String) newValue;
        
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(prefValue);
            
            if (prefIndex >= 0)
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            
        } else {
            preference.setSummary(prefValue);
            
            if (!mIsGetDefaultValues)
                new UpdateWeatherAsyncTask(getActivity()).execute(prefValue);
        }
        
        mIsGetDefaultValues = false;
        
        return true;
    }

    @Override
    public String getFragmentTitle() {
        return getActivity().getString(R.string.action_settings);
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
