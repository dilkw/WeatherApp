package com.example.dz.zscweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.dz.zscweather.gson.Forecast;
import com.example.dz.zscweather.gson.Weather;
import com.example.dz.zscweather.util.HttpUtil;
import com.example.dz.zscweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by DZ on 2019/11/17.
 */

public class WeatherFragment extends Fragment {

    private View rootView;


    private final static String MY_KEY = "b10bd22bc3e14f22be4fd2bd6125ef8e";

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private String mWeatherId;

    private ImageView imageView;

    public SwipeRefreshLayout swipeRefresh;//下拉刷新按钮

    SharedPreferences prefs;

    private int resource_layout;//加载的布局文件

    public String getmWeatherId(){
        return mWeatherId;
    }

    public void setmWeatherId(String mWeatherId){
        this.mWeatherId = mWeatherId;
    }

    public static WeatherFragment newInstance(String mWeatherId){
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("weatherId",mWeatherId);
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        resource_layout = R.layout.weather_fragment;
        if(WeatherActivity.user_cityList.size()== 0){
            resource_layout = R.layout.defualt_weather;
            rootView = inflater.inflate(resource_layout,container,false);
            Button defualtButton = rootView.findViewById(R.id.default_button);
            defualtButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),ChooseAreaActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            rootView = inflater.inflate(resource_layout, container, false);
            Bundle bundle = getArguments();
            this.mWeatherId = bundle.getString("weatherId");
            initView(rootView);
        }
        return rootView;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view,savedInstanceState);
//        initView();
//        Log.d("ddd","初始化WeatherFragment页面");
//    }
    private void initView(View view){
        rootView = view;
        // 初始化各控件
        weatherLayout = (ScrollView) rootView.findViewById(R.id.weather_layout);
        imageView = (ImageView) rootView.findViewById(R.id.bing_pic_img);//背景图片
        titleCity = (TextView) rootView.findViewById(R.id.title_city);
        titleUpdateTime = (TextView) rootView.findViewById(R.id.title_update_time);
        degreeText = (TextView) rootView.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) rootView.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) rootView.findViewById(R.id.forecast_layout);
        aqiText = (TextView) rootView.findViewById(R.id.aqi_text);
        pm25Text = (TextView) rootView.findViewById(R.id.pm25_text);
        comfortText = (TextView) rootView.findViewById(R.id.comfort_text);
        carWashText = (TextView) rootView.findViewById(R.id.car_wash_text);
        sportText = (TextView) rootView.findViewById(R.id.sport_text);
        String bingPic = this.prefs.getString("bing_pic",null);
//        if(bingPic != null){
//            Glide.with(getActivity()).load(bingPic).into(imageView);
//        }else {
            loadBingPic();//加载背景图片
//        }
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(mWeatherId);


        //下拉刷新
        swipeRefresh = rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }



    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + mWeatherId + "&key=" + MY_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText("上次更新时间："+updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.forecast_img);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            imageView.setImageResource(forecastImages(forecast));
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }


    //加载天气图标
    private  int forecastImages(Forecast forecast){
        switch (forecast.more.info){
            case "晴":
                return R.drawable.qing;
            case "阴":
                return R.drawable.yin;
            case "阵雨":
                return R.drawable.zhenyu;
            case "多云":
                return R.drawable.duoyun;
            case "中雨":
                return R.drawable.zhongyu;
            case "雷阵雨":
                return R.drawable.leizhenyu;
            case "小雨":
                return R.drawable.xiaoyu;
            case "大雨":
                return R.drawable.dayu;
            default:return 0;
        }
    }


    //从bing上获取图片作为背景
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingPic).into(imageView);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }




}
