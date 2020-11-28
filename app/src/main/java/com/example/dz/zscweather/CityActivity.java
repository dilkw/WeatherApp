package com.example.dz.zscweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dz.zscweather.db.User_city;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity implements View.OnClickListener,MyLinearLayout.OnScrollListener{

    private MyLinearLayout mLastScrollView;

    private UserCityAdapter userCityAdapter;

    private List<String> cityList = new ArrayList<String>();

    private MyListView myListView;

    private Button cbackButton;

    private Button addButton;

    private int delPosition[] = new int[50];//被删除的位置

    private List<UserCityAdapter.DataHolder> items = new ArrayList<UserCityAdapter.DataHolder>();

    private int array_pos = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_city2);
        items.clear();

        for(int i = 0 ; i < WeatherActivity.user_cityList.size() ; i++){
            UserCityAdapter.DataHolder item = new UserCityAdapter.DataHolder();
            item.title = WeatherActivity.user_cityList.get(i).getUersCityName();
            this.items.add(item);
        }
        addButton = findViewById(R.id.add_city2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Intent intent1 = new Intent(CityActivity.this,ChooseAreaActivity.class);
                intent1.putExtra("data_return",delPosition);
                setResult(RESULT_OK,intent1);
                startActivityForResult(intent1,1);

            }
        });

        cbackButton = (Button) findViewById(R.id.city_back_button2);
        cbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        myListView = (MyListView) findViewById(R.id.city_listview);
        userCityAdapter = new UserCityAdapter(CityActivity.this,this.items,this,this);
        myListView.setAdapter(userCityAdapter);
//        queryUserCity();
    }

    @Override
    public void OnScroll(MyLinearLayout view) {
        if (mLastScrollView != null){
            mLastScrollView.smoothScrollTo(0,0);
        }
        mLastScrollView = view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.del){
            array_pos++;
            int position = myListView.getPositionForView(v);
            DataSupport.deleteAll(User_city.class,"weatherId = ?",WeatherActivity.weatherIdList.get(position));
            userCityAdapter.removeItem(position);
            userCityAdapter.notifyDataSetChanged();
            delPosition[array_pos] = position+1;
            Log.d("uuu","被删除的位置是："+delPosition[array_pos]);

        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        Log.d("111",intent.toString());
        intent.putExtra("data_return",delPosition);
        setResult(RESULT_OK,intent);
        finish();
    }
}
