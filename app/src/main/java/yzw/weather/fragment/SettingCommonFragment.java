package yzw.weather.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;


import yzw.weather.R;

/**
 * Created by yangzhiwei on 2016/10/26.
 */
public class SettingCommonFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_setting_common, container, false);
        RadioButton mRadioButtonh_huashi = (RadioButton)rootview.findViewById(R.id.fragment_setting_common_huashi);
        RadioButton mRadioButtonh_sheshi = (RadioButton)rootview.findViewById(R.id.fragment_setting_common_sheshi);

        final SharedPreferences preferences;
        preferences = getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        mRadioButtonh_huashi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putString("tempertruemode","huashi");
                editor.commit();
            }
        });

        mRadioButtonh_sheshi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putString("tempertruemode","sheshi");
                editor.commit();
            }
        });

        return rootview;

    }


}
