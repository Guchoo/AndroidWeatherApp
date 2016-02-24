package com.guroodesneltvedt.weatherapp.model;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Guro on 21.02.2016.
 */
public class DayForecast {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public Weather weather = new Weather();
    public Forecast forecast = new Forecast();
    public long timestamp;

    public class Forecast {
        public float temp;
        public float temp_min;
        public float temp_max;
        public float pressure;
        public float humidity;
        public String date;
        public String description;
        public String icon;

    }

    public String getStringDate() {
        return sdf.format(new Date(timestamp));
    }
}
