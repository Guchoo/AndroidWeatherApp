package com.guroodesneltvedt.weatherapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guroodesneltvedt.weatherapp.R;
import com.guroodesneltvedt.weatherapp.WeatherHttpClient;
import com.guroodesneltvedt.weatherapp.model.DayForecast;


public class DayForecastFragment extends Fragment {

        private DayForecast dayForecast;
        private ImageView iconWeather;

        public DayForecastFragment() {}

        public void setForecast(DayForecast dayForecast) {

            this.dayForecast = dayForecast;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.content_navigation, container, false);

            TextView tempView = (TextView) v.findViewById(R.id.temp);
            TextView descView = (TextView) v.findViewById(R.id.condDescr);
            tempView.setText( (int) (dayForecast.forecastTemp.temp_min - 275.15) + "-" + (int) (dayForecast.forecastTemp.temp_max - 275.15) );
            descView.setText(dayForecast.weather.currentCondition.getDescr());
            //iconWeather = (ImageView) v.findViewById(R.id.forCondIcon);
            // Now we retrieve the weather icon
            JSONIconWeatherTask task = new JSONIconWeatherTask();
            task.execute(new String[]{dayForecast.weather.currentCondition.getIcon()});

            return v;
        }



        private class JSONIconWeatherTask extends AsyncTask<String, Void, byte[]> {

            @Override
            protected byte[] doInBackground(String... params) {

                byte[] data = null;

                try {

                    // Let's retrieve the icon
                    data = ((new WeatherHttpClient()).getImage(params[0]));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return data;
            }


            @Override
            protected void onPostExecute(byte[] data) {
                super.onPostExecute(data);

                if (data != null) {
                    Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
                    iconWeather.setImageBitmap(img);
                }
            }
        }
}