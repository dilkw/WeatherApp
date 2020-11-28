package com.example.dz.zscweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.dz.zscweather.db.City;
import com.example.dz.zscweather.db.County;
import com.example.dz.zscweather.db.Province;
import com.example.dz.zscweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DZ on 2019/11/12.
 */

public class Utility {

    public static boolean handleProvinceResponse(String response) {
        Log.d("response","请求成功2");
        //判断传入的数据是否为空
        if (!TextUtils.isEmpty(response)) {
            try {
                //省数据的根元素是数组，所以是JSONArray
                JSONArray allProvinces = new JSONArray(response);
                //遍历数组
                for (int i = 0; i < allProvinces.length(); i++) {
                    //得到期中的JSONObject
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    //下面根据key 得到value
                    //并且把数据设置给Province，最后保存到数据库
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                //省数据的根元素是数组，所以是JSONArray
                JSONArray allProvinces = new JSONArray(response);
                //遍历数组
                for (int i = 0; i < allProvinces.length(); i++) {
                    //得到期中的JSONObject
                    JSONObject cityObject = allProvinces.getJSONObject(i);
                    //下面根据key 得到value
                    //并且把数据设置给Province，最后保存到数据库
                    City city = new City();
                    city.setProvinceId(provinceId);
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                //省数据的根元素是数组，所以是JSONArray
                JSONArray allProvinces = new JSONArray(response);
                //遍历数组
                for (int i = 0; i < allProvinces.length(); i++) {
                    //得到期中的JSONObject
                    JSONObject countyObject = allProvinces.getJSONObject(i);
                    //下面根据key 得到value
                    //并且把数据设置给Province，最后保存到数据库
                    County county = new County();
                    county.setCityId(cityId);
                    county.setCountyName(countyObject.getString("name"));
                    county.setId(countyObject.getInt("id"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
