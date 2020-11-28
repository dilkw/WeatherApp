package com.example.dz.zscweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.dz.zscweather.db.User_city;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class WeatherActivity extends AppCompatActivity {

    private ViewPager viewPager;

    public List<WeatherFragment> fragmentList = new ArrayList<WeatherFragment>();//WeatherFragment实例集合

    private WeatherFragmentPageAdapter wAdapter;//WeatherFragment适配器

    public static List<String> weatherIdList = new ArrayList<String>();//城市的天气id

    public static List<User_city> user_cityList;//数据库里查询出来的用户添加过的城市


    private int positionArrary[] = new int[50];//用于记录在CityActivity中被删除的城市在weatherIdList中的位置

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("hhh", data.toString());
        Log.d("ggg", "onActivityResult执行");
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int i = 0;
                    int re_positionArrary[] = data.getIntArrayExtra("data_return");
                    for (int m : re_positionArrary) {
                        Log.d("lll", "第一个元素" + m);
                        if (m != 0) {
                            positionArrary[i] = re_positionArrary[i];
                            i++;
                        } else {
                            break;
                        }
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        if(weatherIdList.size() != wAdapter.getCount()) {

            //当weatherIdList.size() < wAdapter.getCount()时，说明在ChooseAreaActivity中有添加城市的动作
            if(weatherIdList.size() > wAdapter.getCount()) {
                WeatherFragment weatherFragment = WeatherFragment.newInstance(weatherIdList.get(weatherIdList.size() - 1));
                wAdapter.fragmentList.add(weatherFragment);
                wAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(fragmentList.size()-1);
            }

            //当weatherIdList.size() < wAdapter.getCount()时，说明在CityActivity中有删除城市的动作
            else if(positionArrary[0] != 0) {
                for(int m : positionArrary) {
                    Log.d("mmm","positionArrary中元素是" + m );
                    if (m != 0) {
                        wAdapter.fragmentList.remove((m - 1));
                        Log.d("mmm","onResume()中删除的位置是" + ( m - 1 ));
                    } else {
                        break;
                    }
                }
                wAdapter.notifyDataSetChanged();
            }
        }

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从数据库中查询数据来初始化weatherIdList
//        if(Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().setBackgroundDrawableResource(R.drawable.city_title2);
//        }

        WeatherActivity.weatherIdList.clear();
        WeatherActivity.user_cityList = DataSupport.findAll(User_city.class);
        if(WeatherActivity.user_cityList.size() != 0) {
            for (int i = 0; i < WeatherActivity.user_cityList.size(); i++) {
                WeatherActivity.weatherIdList.add(user_cityList.get(i).getWeatherId());
            }
        }

        setContentView(R.layout.activity_weather);
        viewPager = this.findViewById(R.id.id_viewPager);


        /*给fragmentList添加WeatherFragment对象
          WeatherFragment对象通过WeatherFragment中的静态方法ewInstance()获得
        * */
        for (int i = 0; i < WeatherActivity.weatherIdList.size(); i++) {
            this.fragmentList.add(WeatherFragment.newInstance(WeatherActivity.weatherIdList.get(i)));
        }

        //将fragmentList中的WeatherFragment通过WeatherFragmentPageAdapter加载到Viewpager中
        wAdapter = new WeatherFragmentPageAdapter(getSupportFragmentManager(), this.fragmentList);//weatherFragmentList);
        viewPager.setAdapter(wAdapter);
    }



    //加载menu文件并添加点击事件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.city_management:
                Intent intent = new Intent(WeatherActivity.this,CityActivity.class);
                WeatherActivity.this.startActivityForResult(intent,1);
                break;
            default:
        }
        return true;
    }
}

