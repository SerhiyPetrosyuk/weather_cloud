package com.mlsdev.serhiy.weathercloud.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
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

    private TextView mDetailInfoTextView = null;
    
    public DetailWeatherInfoFragment() {
        super.setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        return rootView;
    }
    
    private void findViews(View rootView){
        mDetailInfoTextView = (TextView) rootView.findViewById(R.id.tv_detail_weather);
    }
    
    private void activateViews(){
        Bundle args = getArguments();
        
        if (args != null){
            String detailWeather = args.getString(Constants.DETAIL_WEATHER);
            
            if (detailWeather != null)
                mDetailInfoTextView.setText(detailWeather);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
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
