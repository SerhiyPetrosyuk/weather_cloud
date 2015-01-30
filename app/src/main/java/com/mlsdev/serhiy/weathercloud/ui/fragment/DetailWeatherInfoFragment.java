package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.support.v7.app.ActionBarActivity;

import com.mlsdev.serhiy.weathercloud.ui.activity.BaseActivity;

/**
 * Created by android on 29.01.15.
 */
public class DetailWeatherInfoFragment extends Fragment {

    public DetailWeatherInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity activity = (BaseActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Detail fragment");
        activity.activateBackButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
