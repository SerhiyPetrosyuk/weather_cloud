package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by android on 30.01.15.
 */
public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }


    public abstract void getWeatherForecast(boolean isUpdateForecast);
    
}
