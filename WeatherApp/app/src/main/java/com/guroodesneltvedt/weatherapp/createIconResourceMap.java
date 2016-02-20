package com.guroodesneltvedt.weatherapp;

import java.util.HashMap;

public class createIconResourceMap {

    public void createIconResourceMap() {
        iconResourceIdMap = new HashMap<String, Integer>();
        iconResourceIdMap.put("01d.png", R.drawable.w01d);//clear sky
        iconResourceIdMap.put("01n.png",R.drawable.w01n);//clear sky night
        iconResourceIdMap.put("02d.png",R.drawable.w02d);//few clouds
        iconResourceIdMap.put("02n.png",R.drawable.w02n);//few clouds night
        iconResourceIdMap.put("03d.png",R.drawable.w03d);//scattered clouds
        iconResourceIdMap.put("03n.png",R.drawable.w03n);//scattered clouds night
        iconResourceIdMap.put("04d.png",R.drawable.w04d);//broken clouds
        iconResourceIdMap.put("04n.png",R.drawable.w04n);//broken clouds night

        iconResourceIdMap.put("09d.png",R.drawable.w09d);//shower rain
        iconResourceIdMap.put("09n.png",R.drawable.w09n);//shower rain night
        iconResourceIdMap.put("10d.png",R.drawable.w10d);//rain
        iconResourceIdMap.put("10n.png",R.drawable.w10n);//rain night
        iconResourceIdMap.put("11d.png",R.drawable.w11d);//THUNDERSTORM
        iconResourceIdMap.put("11n.png",R.drawable.w11n);//THUNDERSTORM night
        iconResourceIdMap.put("13d.png",R.drawable.w13d);//SNOW
        iconResourceIdMap.put("13n.png",R.drawable.w13n);//SNOW night

        iconResourceIdMap.put("50d.png",R.drawable.w50d);//bacon (mist)
        iconResourceIdMap.put("50n.png",R.drawable.w50n);//bacon night(mist)
    }

    private HashMap<String,Integer> iconResourceIdMap = new HashMap<String, Integer>();

    public int convertOpenWeatherIconIdToResourceIconId(String OWicon){
        Integer resourceId = iconResourceIdMap.get(OWicon+".png");
        if(resourceId==null)
            return R.drawable.weather_default;
        return resourceId;
    }

}
