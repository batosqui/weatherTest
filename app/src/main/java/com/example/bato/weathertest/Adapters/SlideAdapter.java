package com.example.bato.weathertest.Adapters;

import android.content.Context;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bato.weathertest.MainActivity;
import com.example.bato.weathertest.R;
import com.example.bato.weathertest.entity.WeatherData;
import com.example.bato.weathertest.utils.Util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;


    public SlideAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return this.context.getResources().getStringArray(R.array.lst_locations).length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.weather_slider, container, false);
        ArrayList<WeatherData> weatherArray = MainActivity.getWeatherData();
        if (weatherArray != null && weatherArray.size() > 0) {
            TextView title = (TextView) view.findViewById(R.id.txtTitle);
            TextView windDir = (TextView) view.findViewById(R.id.windDirection);
            TextView windSpeed = (TextView) view.findViewById(R.id.windSpeed);
            ImageView img = (ImageView) view.findViewById(R.id.ImgIcon);
            TextView temp = (TextView) view.findViewById(R.id.ActualTemp);
            TextView tempMax = (TextView) view.findViewById(R.id.TempMax);
            TextView tempMin = (TextView) view.findViewById(R.id.TempMin);
            TextView dayDescription = (TextView) view.findViewById(R.id.dayDescription);
            TextView dayInfo = (TextView) view.findViewById(R.id.weatherDescription);
            title.setText(weatherArray.get(position).getName());
            temp.setText(String.valueOf(weatherArray.get(position).getMain().getTemp()) + "°C");
            tempMax.setText(String.valueOf(weatherArray.get(position).getMain().getTemp_max()) + "°C");
            tempMin.setText(String.valueOf(weatherArray.get(position).getMain().getTemp_min()) + "°C");
            dayDescription.setText(weatherArray.get(position).getWeather().getMain());
            dayInfo.setText(weatherArray.get(position).getWeather().getDescription());
            windDir.setText(Util.getWindDirection(weatherArray.get(position).getWind().getDeg()));
            windSpeed.setText(String.valueOf(weatherArray.get(position).getWind().getSpeed())+" m/sec");
            /*Matrix mat = new Matrix();
            //mat.postRotate(Integer.parseInt());*/
            img.setImageBitmap(weatherArray.get(position).getWeather().getIconBitmap());


        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

}
