package com.example.bato.weathertest.utils;

public final class Util {
    private static String NORTH = "N";
    private static String SOUTH = "S";
    private static String WEST = "W";
    private static String EAST = "E";
    private static String NORTH_WEST = "NW";
    private static String NORTH_EAST = "NE";
    private static String SOUTH_WEST = "SW";
    private static String SOUTH_EAST = "SE";
    private static String NO_WIND = "No direction";

    public static String getWindDirection(int deg) {
        String windDirection = "";
        if (deg >= 326.26 || deg <= 11.25) {
            windDirection = SOUTH;
        } else if (deg >= 11.26 && deg <= 56.25) {
            windDirection = NORTH_EAST;
        } else if (deg >= 50.26 && deg <= 101.25) {
            windDirection = EAST;
        } else if (deg >= 101.26 && deg <= 146.25) {
            windDirection = SOUTH_EAST;
        } else if (deg >= 146.26 && deg <= 191.25) {
            windDirection = SOUTH;
        } else if (deg >= 191.26 && deg <= 236.25) {
            windDirection = SOUTH_WEST;
        } else if (deg >= 236.26 && deg <= 281.25) {
            windDirection = WEST;
        } else if (deg >= 281.26 && deg < 326.25) {
            windDirection = NORTH_WEST;
        }else{
            windDirection=NO_WIND;
        }
        return windDirection;
    }
}
