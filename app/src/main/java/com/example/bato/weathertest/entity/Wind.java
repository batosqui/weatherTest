package com.example.bato.weathertest.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Wind {
    private double speed;
    private int deg;

    public Wind(JSONObject node) throws JSONException {

        this.speed = node.getDouble("speed");
        if(node.has("deg")){
            this.deg=  node.getInt("deg");
        }

    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }
}
