package com.guroodesneltvedt.weatherapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Guro on 21.02.2016.
 */
public class DayForecast {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public Weather weather = new Weather();
    public ForecastTemp forecastTemp = new ForecastTemp();
    public long timestamp;

    public class ForecastTemp {
        public float temp;
        public float temp_min;
        public float temp_max;
        public float pressure;
        public float humidity;
        public String date;

//        public float day;
//        public float min;
//        public float max;
//        public float night;
//        public float eve;
//        public float morning;
    }

    public String getStringDate() {
        return sdf.format(new Date(timestamp));
    }
}
