package yzw.weather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import yzw.weather.fragment.SettingCommonFragment;

/**
 * Created by yangzhiwei on 2016/10/26.
 */
public class MyPageAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private List<SettingCommonFragment> mFragments;

    public MyPageAdapter(FragmentManager fm , String[] mTitles, List<SettingCommonFragment> mFragments) {
        super (fm);
        this. mTitles = mTitles;
        this. mFragments = mFragments;
    }

    @Override
    public CharSequence getPageTitle( int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}