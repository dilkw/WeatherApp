package com.example.dz.zscweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserCityAdapter extends BaseAdapter {

    private View.OnClickListener mDelClickListener;

    private MyLinearLayout.OnScrollListener mScrollListener;

    public static interface OnScrollListener{
        public void OnScroll(MyLinearLayout view);
    }


    private Context mContext;

    private ArrayList<String> user_cities;

    private List<DataHolder> mDataList = new ArrayList<DataHolder>();

    private Button curDel_btn;

    private LayoutInflater mInflater;

    private View mView;

    private float x, ux;

    public UserCityAdapter(Context context, List<DataHolder> mDataList, View.OnClickListener delClickListener, MyLinearLayout.OnScrollListener listener) {
        for(int i = 0 ; i < mDataList.size() ; i++){
            Log.d("yyy",mDataList.get(i).title);
        }
        this.mDataList.clear();
        this.mDataList = mDataList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mDelClickListener = delClickListener;
        mScrollListener = listener;
    }


    public int getCount() {
        return this.mDataList.size();
    }

    public Object getItem(int position) {
        return mDataList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = this.mInflater.inflate(R.layout.my_listview_item,parent,false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView)convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DataHolder item = mDataList.get(position);

        holder.tvTitle.setText(item.title);

        item.rootView = (LinearLayout)convertView.findViewById(R.id.lin_root);
        item.rootView.scrollTo(0,0);


        Button delTv = (Button) convertView.findViewById(R.id.del);
        delTv.setOnClickListener(mDelClickListener);

        this.mView = convertView;
        return convertView;
    }


    public void removeItem(int position){
        mDataList.remove(position);this.notifyDataSetChanged();
        //更新信息
        WeatherActivity.weatherIdList.remove(position);
        WeatherActivity.user_cityList.remove(position);

    }

    final static class ViewHolder {//删除按键视图
        TextView tvTitle;
        Button btnDel;
    }

    public static class DataHolder {//添加过的城市的视图
        public String title;
        public LinearLayout rootView;
    }
}