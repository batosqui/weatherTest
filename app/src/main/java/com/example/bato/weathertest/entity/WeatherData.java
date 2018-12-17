package com.example.bato.weathertest.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {

private double lon;
private double lat;
private Main main;
private Weather weather;
    private Wind wind;
private String name;


    public WeatherData(JSONObject json) throws JSONException {

        JSONObject coords = json.getJSONObject("coord");
        this.lon = coords.getDouble("lon");
        this.lat = coords.getDouble("lat");
        this.main = new Main(json.getJSONObject("main"));
        this.weather = new Weather(json.getJSONArray("weather"));
        this.wind= new Wind(json.getJSONObject("wind"));
        this.name = json.getString("name");
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
