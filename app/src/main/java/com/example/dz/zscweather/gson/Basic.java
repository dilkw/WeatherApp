package com.example.dz.zscweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DZ on 2019/11/13.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}

