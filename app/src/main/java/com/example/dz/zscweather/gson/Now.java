package com.example.dz.zscweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DZ on 2019/11/15.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }


}
