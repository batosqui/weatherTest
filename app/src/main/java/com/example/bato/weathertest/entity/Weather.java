package com.example.bato.weathertest.entity;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather {

    private int id;
    private String main;
    private String description;
    private String icon;
    private Bitmap iconBitmap;
    public Weather(JSONArray node) throws JSONException {

        JSONObject weather = node.getJSONObject(0);
        this.id = weather.getInt("id");
        this.main = weather.getString("main");
        this.description = weather.getString("description");
        this.icon = weather.getString("icon");

    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }
}
