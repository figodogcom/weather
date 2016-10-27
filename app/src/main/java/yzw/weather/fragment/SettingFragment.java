package yzw.weather.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import yzw.weather.MyPageAdapter;
import yzw.weather.R;

/**
 * Created by yangzhiwei on 2016/10/26.
 */
public class SettingFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        String[] tabString = { "通用" ,"关于"};
        ArrayList<SettingCommonFragment> mFragments = new ArrayList<>();
        for ( int i = 0 ; i < tabString. length ; i++) {
            SettingCommonFragment mFragment = new SettingCommonFragment() ;
            mFragments.add(mFragment) ;
        }
        TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.viewpager);

        MyPageAdapter mViewPagerAdapter = new MyPageAdapter(getActivity().getSupportFragmentManager() , tabString, mFragments) ;
        viewPager.setAdapter(mViewPagerAdapter);
//将Tab与ViewPager关联（动画效果同步）
        tabLayout.setupWithViewPager(viewPager ) ;


        return rootView;

    }









}
