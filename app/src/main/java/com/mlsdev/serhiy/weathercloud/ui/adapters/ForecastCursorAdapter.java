package com.mlsdev.serhiy.weathercloud.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlsdev.serhiy.weathercloud.R;
import com.mlsdev.serhiy.weathercloud.data.WeatherContract;
import com.mlsdev.serhiy.weathercloud.ui.activity.MainActivity;
import com.mlsdev.serhiy.weathercloud.util.Utility;

/**
 * Created by android on 07.02.15.
 */
public class ForecastCursorAdapter extends CursorAdapter {
    
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    public ForecastCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0  && !MainActivity.TWO_PANE) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = (viewType == VIEW_TYPE_TODAY) ? R.layout.today_list_item : R.layout.new_list_item;
        
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.viewType = (viewType == VIEW_TYPE_TODAY) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
        itemView.setTag(viewHolder);
        
        return itemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        
        int weatherId = cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));
        String dateString = cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATETEXT));
        String description = cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC));
        boolean isMetricUnits = Utility.isMetricUnits(context);
        double maxTemp = cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
        double minTemp = cursor.getDouble(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));

        // Handling with View Holder
        int imageId = (viewHolder.viewType == VIEW_TYPE_TODAY && !MainActivity.TWO_PANE) ? Utility.getArtResourceForWeatherCondition(weatherId)
                : Utility.getICResourceForWeatherCondition(weatherId);
        viewHolder.iconView.setImageResource(imageId);
        viewHolder.dateTextView.setText(Utility.getFriendlyDayString(context, dateString));
        viewHolder.descriptionTextView.setText(description);
        viewHolder.maxTempTextView.setText(Utility.formatTemperature(maxTemp, isMetricUnits) + context.getString(R.string.degree_sign));
        viewHolder.minTempTextView.setText(Utility.formatTemperature(minTemp, isMetricUnits) + context.getString(R.string.degree_sign));
    }
    
    public static class ViewHolder{
        public ImageView iconView;
        public TextView dateTextView;
        public TextView descriptionTextView;
        public TextView maxTempTextView;
        public TextView minTempTextView;
        public int viewType = 0;

        public ViewHolder(View view) {
            iconView            = (ImageView) view.findViewById(R.id.iv_weather_icon);
            dateTextView        = (TextView) view.findViewById(R.id.tv_date_in_list_item);
            descriptionTextView = (TextView) view.findViewById(R.id.tv_weather_in_list_item);
            maxTempTextView     = (TextView) view.findViewById(R.id.tv_max_temp_in_list_item);
            minTempTextView     = (TextView) view.findViewById(R.id.tv_min_temp_in_list_item);
        }
    }
}
