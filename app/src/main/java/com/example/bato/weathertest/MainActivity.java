package com.example.bato.weathertest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bato.weathertest.Adapters.SlideAdapter;
import com.example.bato.weathertest.entity.Main;
import com.example.bato.weathertest.entity.WeatherData;
import com.example.bato.weathertest.utils.HttpHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    protected String url_by_id = "http://api.openweathermap.org/data/2.5/weather?id=";
    protected String url_img = "http://openweathermap.org/img/w/";
    protected String IMG_EXTENSION = ".png";
    protected String API_KEY = "32c6d49435b4d444963dbe7aa2bfdf98";
    public String mode = "metric";
    String TAG = "MainActivity";
    private SlideAdapter myadapter;
    private static ArrayList<WeatherData> weatherArray;
    WeatherData weatherData;
    private ProgressBar spinner;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    // Declaring a Location Manager
    protected LocationManager mLocationManager;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (ProgressBar) findViewById(R.id.loading);
        Button btnGPS = (Button) findViewById(R.id.findGPS);

        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivityIntent = new Intent(MainActivity.this, WeatherByGPS.class);
                startActivity(startActivityIntent);
            }
        });

        new GetWeather().execute();
        myadapter = new SlideAdapter(getApplicationContext());

    }


    public static ArrayList<WeatherData> getWeatherData() {
        return MainActivity.weatherArray;
    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private class GetWeather extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Descargando Json", Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Invocar a un servicio o realizar una accion en un hilo de fondo
            HttpHandler handler = new HttpHandler();

            // Realizar llamada a url y obtener respuesta
            String[] mIdsArray;
            weatherArray = new ArrayList<WeatherData>();
            mIdsArray = getResources().getStringArray(R.array.lst_locations);
            for (String id : mIdsArray) {

                Log.i("URL", url_by_id + id + "&units=" + mode + "&appid=" + API_KEY);
                String jsonStr = handler.makeServiceCall(url_by_id + id + "&units=" + mode + "&appid=" + API_KEY + "&lang=es");


                Log.i(TAG, "Respuesta de api: " + jsonStr);
                if (jsonStr != null) {
                    try {

                        //JSONArray jsonArray = new JSONArray(jsonStr);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        weatherData = new WeatherData(jsonObj);

                        URL url = new URL(url_img + weatherData.getWeather().getIcon() + IMG_EXTENSION);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        weatherData.getWeather().setIconBitmap(image);

                        weatherArray.add(weatherData);
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


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPager.removeAllViews();
            viewPager.setAdapter(myadapter);
            viewPager.setCurrentItem(0);

            spinner.setVisibility(View.GONE);

        }


    }
}
