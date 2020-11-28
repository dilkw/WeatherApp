package com.example.dz.zscweather.db;

import org.litepal.crud.DataSupport;

public class User_city extends DataSupport {


    private int id;
    private String uersCityName;
    private String weatherId;

    public String getUersCityName() {
        return uersCityName;
    }

    public void setUersCityName(String uersCityName) {
        this.uersCityName = uersCityName;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
