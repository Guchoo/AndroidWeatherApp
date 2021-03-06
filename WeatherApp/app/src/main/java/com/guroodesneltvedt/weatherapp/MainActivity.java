package com.guroodesneltvedt.weatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.guroodesneltvedt.weatherapp.model.DayForecast;
import com.guroodesneltvedt.weatherapp.model.Weather;
import com.guroodesneltvedt.weatherapp.model.WeatherForecast;

import org.json.JSONException;

import java.util.ArrayList;

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
    private TextView date2;
    private TextView prevTemp2;
    private TextView prevDescr2;
    private TextView date3;
    private TextView prevTemp3;
    private TextView prevDescr3;
    private TextView date4;
    private TextView prevTemp4;
    private TextView prevDescr4;
    private TextView date5;
    private TextView prevTemp5;
    private TextView prevDescr5;

    private ArrayList iconList = new ArrayList();
    private ArrayList dateList = new ArrayList();
    private ArrayList prevTempList = new ArrayList();
    private ArrayList prevDescrList = new ArrayList();

    private static String forecastDaysNum = "5";
    private LocationManager locManager;
    createIconResourceMap createIcon = new createIconResourceMap();
    private SharedPreferences sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TableLayout tablelay = (TableLayout) findViewById(R.id.table);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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

        for(int i = 0, j = tablelay.getChildCount(); i < j; i++) {
            View view = tablelay.getChildAt(i);
            final int f = i;
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createDialogBox(f);
                    }
                });
            }
        }
        //Get content from search.
//        edt.setOnEditorActionListener(new TextView.OnEditorActionListener(){
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    JSONWeatherTask task = new JSONWeatherTask();
//                    String pattern = edt.getEditableText().toString();
//                    task.execute(new String[]{pattern});
//
//                    JSONForecastWeatherTask task2 = new JSONForecastWeatherTask();
//                    task2.execute(new String[]{pattern});
//                    return true;
//                }
//                return false;
//            }
//        });



    }

    public void createDialogBox(int i){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.details_dialog);
        dialog.setTitle("More details: ");
        String iconString = (String) iconList.get(i);
        ImageView iv = (ImageView) dialog.findViewById(R.id.imageDetail);

        Integer intIcon = createIcon.convertOpenWeatherIconIdToResourceIconId(iconString);
        iv.setImageResource(intIcon);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
        date2 = (TextView) findViewById(R.id.date2);
        prevTemp2 = (TextView) findViewById(R.id.prevTemp2);
        prevDescr2 = (TextView) findViewById(R.id.desc2);
        date3 = (TextView) findViewById(R.id.date3);
        prevTemp3 = (TextView) findViewById(R.id.prevTemp3);
        prevDescr3 = (TextView) findViewById(R.id.desc3);
        date4 = (TextView) findViewById(R.id.date4);
        prevTemp4 = (TextView) findViewById(R.id.prevTemp4);
        prevDescr4 = (TextView) findViewById(R.id.desc4);
        date5 = (TextView) findViewById(R.id.date5);
        prevTemp5 = (TextView) findViewById(R.id.prevTemp5);
        prevDescr5 = (TextView) findViewById(R.id.desc5);

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
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        } else if (id == R.id.nav_berlin) {
            city = "Berlin,DE";
        } else if (id == R.id.nav_stavanger) {
            city = "Stavanger,NO";
        }
        else{

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        getWeather(city, lang);
        shareEdit.putString("city", city);
        shareEdit.commit();
        shareEdit.putString("cityLang", lang);
        shareEdit.commit();
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
                Integer icon = createIcon.convertOpenWeatherIconIdToResourceIconId(weather.currentCondition.getIcon());
                imgView.setImageResource(icon);
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
                // Let's retrieve the icon
                System.out.println("Weather [" + forecast + "]");
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;

        }


        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);
            iconList.clear();
            for(int i=0; i< 5; i++){
                iconList.add(forecastWeather.daysForecast.get(i).forecast.icon);
                dateList.add("" + forecastWeather.daysForecast.get(i).forecast.date);
                prevTempList.add("" + Math.round((forecastWeather.daysForecast.get(i).forecast.temp - 273.15))  + "\u00b0" + "C");
                prevDescrList.add("" + forecastWeather.daysForecast.get(i).forecast.description);
            }



            //Should be prettier... Moved to the loop above
            String date_1 = "" + forecastWeather.daysForecast.get(0).forecast.date;
            String d1 = date_1.substring(5, 13);
            String date_2 = "" + forecastWeather.daysForecast.get(1).forecast.date;
            String d2 = date_2.substring(5, 13);
            String date_3 = "" + forecastWeather.daysForecast.get(2).forecast.date;
            String d3 = date_3.substring(5, 13);
            String date_4 = "" + forecastWeather.daysForecast.get(3).forecast.date;
            String d4 = date_4.substring(5, 13);
            String date_5 = "" + forecastWeather.daysForecast.get(4).forecast.date;
            String d5 = date_5.substring(5, 13);

            date.setText(d1 + "h");
            prevTemp.setText("" + Math.round((forecastWeather.daysForecast.get(0).forecast.temp - 273.15))  + "\u00b0" + "C");
            prevDescr.setText("" + forecastWeather.daysForecast.get(0).forecast.description);
            date2.setText(d2 + "h");
            prevTemp2.setText("" + Math.round((forecastWeather.daysForecast.get(1).forecast.temp - 273.15))  + "\u00b0" + "C");
            prevDescr2.setText("" + forecastWeather.daysForecast.get(1).forecast.description);
            date3.setText(d3 + "h");
            prevTemp3.setText("" + Math.round((forecastWeather.daysForecast.get(2).forecast.temp - 273.15))  + "\u00b0" + "C");
            prevDescr3.setText("" + forecastWeather.daysForecast.get(2).forecast.description);
            date4.setText(d4 + "h");
            prevTemp4.setText("" + Math.round((forecastWeather.daysForecast.get(3).forecast.temp - 273.15))  + "\u00b0" + "C");
            prevDescr4.setText("" + forecastWeather.daysForecast.get(3).forecast.description);
            date5.setText(d5 + "h");
            prevTemp5.setText("" + Math.round((forecastWeather.daysForecast.get(4).forecast.temp - 273.15))  + "\u00b0" + "C");
            prevDescr5.setText("" + forecastWeather.daysForecast.get(4).forecast.description);
        }
    }
    private static Criteria searchProviderCriteria = new Criteria();
    // Location Criteria
        static
    {
        searchProviderCriteria.setPowerRequirement(Criteria.POWER_LOW);
        searchProviderCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        searchProviderCriteria.setCostAllowed(false);
    }
    private boolean checkWriteExternalPermission()
    {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    // Get city by GPS or WiFi. Not done.
//    public void getPosition() {
//        String provider = locManager.getBestProvider(searchProviderCriteria, true);
//        checkWriteExternalPermission();
//        Location loc = locManager.getLastKnownLocation(provider);
//
//        if (loc == null) {
//            // We request another update Location
//            Log.d("SwA", "Request location");
//            locManager.requestSingleUpdate(provider, locListener, null);
//        } else {
//            JSONWeatherTask task = new JSONWeatherTask();
//            task.execute(new String[]{loc});
//        }
//    }
//
//    public LocationListener locListener = new LocationListener() {
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.d("SwA", "Location changed!");
//            String sLat =   "" + location.getLatitude();
//            String sLon =  "" + location.getLongitude();
//            Log.d("SwA", "Lat [" + sLat + "] - sLong [" + sLon + "]");
//
//            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            checkWriteExternalPermission();
//            locManager.removeUpdates(locListener);
//            JSONWeatherTask task = new JSONWeatherTask();
//            task.execute(new String[]{location});
//        }
//    };

}
