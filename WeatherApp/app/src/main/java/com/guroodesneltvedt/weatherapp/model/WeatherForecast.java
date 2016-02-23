package com.guroodesneltvedt.weatherapp.model;

import com.survivingwithandroid.weather.lib.model.CurrentWeather;

import java.util.ArrayList;
import java.util.List;


public class WeatherForecast {
    public List<DayForecast> daysForecast = new ArrayList<DayForecast>();
//    public CurrentConditionForecast currentConditionForecast = new CurrentConditionForecast();
//    public Temperature temperature = new Temperature();

    public void addForecast(DayForecast forecast) {
        daysForecast.add(forecast);
        System.out.println("Add forecast ["+forecast+"]");
    }

    public DayForecast getForecast(int dayNum) {
        return daysForecast.get(dayNum);
    }

//    public class CurrentConditionForecast{
//        private int weatherId;
//        private String condition;
//        private String descr;
//        private String icon;
//
//
//        private float pressure;
//        private float humidity;
//
//        public int getWeatherId() {
//            return weatherId;
//        }
//        public void setWeatherId(int weatherId) {
//            this.weatherId = weatherId;
//        }
//        public String getCondition() {
//            return condition;
//        }
//        public void setCondition(String condition) {
//            this.condition = condition;
//        }
//        public String getDescr() {
//            return descr;
//        }
//        public void setDescr(String descr) {
//            this.descr = descr;
//        }
//        public String getIcon() {
//            return icon;
//        }
//        public void setIcon(String icon) {
//            this.icon = icon;
//        }
//        public float getPressure() {
//            return pressure;
//        }
//        public void setPressure(float pressure) {
//            this.pressure = pressure;
//        }
//        public float getHumidity() {
//            return humidity;
//        }
//        public void setHumidity(float humidity) {
//            this.humidity = humidity;
//        }
//    }
//
//    public  class Temperature {
//        private float temp;
//        private float minTemp;
//        private float maxTemp;
//
//        public float getTemp() {
//            return temp;
//        }
//        public void setTemp(float temp) {
//            this.temp = temp;
//        }
//        public float getMinTemp() {
//            return minTemp;
//        }
//        public void setMinTemp(float minTemp) {
//            this.minTemp = minTemp;
//        }
//        public float getMaxTemp() {
//            return maxTemp;
//        }
//        public void setMaxTemp(float maxTemp) {
//            this.maxTemp = maxTemp;
//        }
//
//    }
}
