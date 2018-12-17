package com.example.bato.weathertest;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bato.weathertest.entity.WeatherData;
import com.example.bato.weathertest.utils.HttpHandler;
import com.example.bato.weathertest.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WeatherByGPS extends AppCompatActivity  implements LocationListener{

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected LocationManager mLocationManager;
    private Location mLocation;
    String TAG = "WeatherByGPS";

    protected String url_by_gps = "http://api.openweathermap.org/data/2.5/weather?";
    protected String url_img = "http://openweathermap.org/img/w/";
    protected String IMG_EXTENSION = ".png";
    protected String API_KEY = "32c6d49435b4d444963dbe7aa2bfdf98";
    public String mode = "metric";

    WeatherData weatherData;
    private ProgressBar spinner;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_by_gps);
        spinner = (ProgressBar) findViewById(R.id.loading_gps);
        spinner.setVisibility(View.VISIBLE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = mLocationManager.getBestProvider(new Criteria(), false);
        if(checkLocationPermission()){
            mLocationManager.requestLocationUpdates(provider,400,400,this);
        };

    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.permission_message_title)
                        .setMessage(R.string.permission_message)
                        .setPositiveButton(R.string.permission_message_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(WeatherByGPS.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        provider=mLocationManager.getBestProvider(new Criteria(), false);
                        mLocationManager.requestLocationUpdates(provider, 400, 1, this) ;
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation= location;
        mLocationManager.removeUpdates(this);
        new GetWeather().execute();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        showGPSDisabledAlertToUser();
    }


    private class GetWeather extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(WeatherByGPS.this, "Descargando Json", Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Invocar a un servicio o realizar una accion en un hilo de fondo
            HttpHandler handler = new HttpHandler();

            // Realizar llamada a url y obtener respuesta
            String[] mIdsArray;
            Log.i("URL", url_by_gps + "lat=" + mLocation.getLatitude() + "&lon=" + mLocation.getLongitude() + "&units=" + mode + "&appid=" + API_KEY);
            String jsonStr = handler.makeServiceCall(url_by_gps + "lat=" + mLocation.getLatitude() + "&lon=" + mLocation.getLongitude() + "&units=" + mode + "&appid=" + API_KEY + "&lang=es");
            Log.i(TAG, "Respuesta de api: " + jsonStr);
            if (jsonStr != null) {
                try {

                    //JSONArray jsonArray = new JSONArray(jsonStr);
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    weatherData = new WeatherData(jsonObj);

                    URL url = new URL(url_img + weatherData.getWeather().getIcon() + IMG_EXTENSION);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    weatherData.getWeather().setIconBitmap(image);
//url.openConnection().
                    Log.i("AG", "Finaliza seteo");
                    url.openConnection().getInputStream().close();
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "No es posible obtener json del server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "No es posible obtener json del server. Revisar LogCat por posibles errores!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (weatherData != null) {
                TextView title = (TextView) findViewById(R.id.txtTitle_gps);
                TextView windDir = (TextView) findViewById(R.id.windDirection_gps);
                TextView windSpeed = (TextView) findViewById(R.id.windSpeed_gps);
                ImageView img = (ImageView) findViewById(R.id.ImgIcon_gps);
                TextView temp = (TextView) findViewById(R.id.ActualTemp_gps);
                TextView tempMax = (TextView) findViewById(R.id.TempMax_gps);
                TextView tempMin = (TextView) findViewById(R.id.TempMin_gps);
                TextView dayDescription = (TextView) findViewById(R.id.dayDescription_gps);
                TextView dayInfo = (TextView) findViewById(R.id.weatherDescription_gps);
                title.setText(weatherData.getName());
                temp.setText(String.valueOf(weatherData.getMain().getTemp()) + "°C");
                tempMax.setText(String.valueOf(weatherData.getMain().getTemp_max()) + "°C");
                tempMin.setText(String.valueOf(weatherData.getMain().getTemp_min()) + "°C");
                dayDescription.setText(weatherData.getWeather().getMain());
                dayInfo.setText(weatherData.getWeather().getDescription());
                windDir.setText(Util.getWindDirection(weatherData.getWind().getDeg()));
                windSpeed.setText(String.valueOf(weatherData.getWind().getSpeed())+" m/sec");
            /*Matrix mat = new Matrix();
            //mat.postRotate(Integer.parseInt());*/
                img.setImageBitmap(weatherData.getWeather().getIconBitmap());


            }


            spinner.setVisibility(View.GONE);

        }


    }


}
