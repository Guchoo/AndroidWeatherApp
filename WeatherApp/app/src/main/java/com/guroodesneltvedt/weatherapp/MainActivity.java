package com.guroodesneltvedt.weatherapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.guroodesneltvedt.weatherapp.adapter.DailyForecastPageAdapter;
import com.guroodesneltvedt.weatherapp.model.DayForecast;
import com.guroodesneltvedt.weatherapp.model.Weather;
import com.guroodesneltvedt.weatherapp.model.WeatherForecast;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private TextView hum;
    private ImageView imgView;
    private TextView date;
    private TextView prevTemp;
    private TextView prevDescr;

    private static String forecastDaysNum = "5";

    createIconResourceMap createIcon = new createIconResourceMap();
    private SharedPreferences sharePref;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        createIcon.createIconResourceMap();
        sharePref = getSharedPreferences("cityLabel", 0);
        sharePref = getSharedPreferences("cityLang", 1);
        if (sharePref.contains("city")) {
            String city = sharePref.getString("city", "");
            sharePref.edit().remove("city").commit();
            String lang = sharePref.getString("lang", "");
            sharePref.edit().remove("lang").commit();
            getWeather(city, lang);
        } else {
            startInfo();
        }
    }

    public void startInfo() {
        String text = "Select city in the menu by clicking @";
        SpannableString str = new SpannableString(text);
        int index = str.length() - 1;
        ImageSpan imagespan = new ImageSpan(this, R.drawable.ic_menu_24dp);
        str.setSpan(imagespan, index, index + 1, ImageSpan.ALIGN_BASELINE);

        new AlertDialog.Builder(this)
                .setTitle("How to start")
                .setMessage(str)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    public void getWeather(String city, String lang) {

        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condDescr);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.condIcon);

        date = (TextView) findViewById(R.id.date);
        prevTemp = (TextView) findViewById(R.id.prevTemp);
        prevDescr = (TextView) findViewById(R.id.desc);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});

        JSONForecastWeatherTask task2 = new JSONForecastWeatherTask();
        task2.execute(new String[]{city,lang, forecastDaysNum});

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        SharedPreferences.Editor shareEdit = sharePref.edit();

        int id = item.getItemId();
        String city = "";
        String lang = "";
        if (id == R.id.nav_grimstad) {
            city = "Grimdtad,NO";
        } else if (id == R.id.nav_oslo) {
            city = "Oslo,NO";
        } else if (id == R.id.nav_bergen) {
            city = "Bergen,NO";
        } else if (id == R.id.nav_london) {
            city = "London,UK";
            lang = "en";
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        getWeather(city, lang);
//        shareEdit.putString("city", city);
//        shareEdit.commit();
//        shareEdit.putString("cityLang", lang);
//        shareEdit.commit();
        return true;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Retrieve the icon
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            } else {
                Integer test = createIcon.convertOpenWeatherIconIdToResourceIconId(weather.currentCondition.getIcon());
                imgView.setImageResource(test);
            }

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "\u00b0" + "C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "\u00b0");

        }
    }

    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {

        @Override
        protected WeatherForecast doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
            WeatherForecast forecast = new WeatherForecast();

            try {
                forecast = JSONWeatherParser.getForecastWeather(data);
                System.out.println("Weather [" + forecast + "]");
                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;

        }


        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);

            String myString = "" + forecastWeather.daysForecast.get(0).forecastTemp.date;
            String dateString = myString.substring(5, 10);


            date.setText(dateString);
            prevTemp.setText("" + Math.round((forecastWeather.daysForecast.get(0).forecastTemp.temp - 273.15))  + "\u00b0" + "C");
            prevDescr.setText("" + forecastWeather.currentConditionForecast.getDescr());

        }
    }
}
