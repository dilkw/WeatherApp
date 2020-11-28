package com.example.dz.zscweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DZ on 2019/11/15.
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
