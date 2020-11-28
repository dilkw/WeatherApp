package com.example.dz.zscweather;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by DZ on 2019/11/17.
 */

public class WeatherFragmentPageAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mFragmentManager;

    public List<WeatherFragment> fragmentList;

    private Fragment fragment = null;

    public List<WeatherFragment> getFragmentList() {
        return fragmentList;
    }

    public WeatherFragmentPageAdapter(FragmentManager fm, List<WeatherFragment> fragmentList) {
        super(fm);
        this.fragmentList= fragmentList;
        this.notifyDataSetChanged();
    }


    @Override
    public WeatherFragment getItem(int position) {

        return this.fragmentList.get(position);
        //return WeatherFragment.newInstance(WeatherActivity.weatherIdList.get(position));
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container,position,object);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof Fragment) {

            if (fragmentList.contains(object)) {
                return fragmentList.indexOf(object);
            } else {
                return POSITION_NONE;
            }

        }
        return super.getItemPosition(object);
    }


    /**
     * This method should be called by the application if the data backing this adapter has changed
     * and associated views should update.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
