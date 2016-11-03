package yzw.weather.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
        RadioButton mRadioButton_huashi = (RadioButton)rootview.findViewById(R.id.fragment_setting_common_huashi);
        RadioButton mRadioButton_sheshi = (RadioButton)rootview.findViewById(R.id.fragment_setting_common_sheshi);

        final SharedPreferences preferences;
        preferences = getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        if(preferences.getString("tempertruemode",null).equals("sheshi"))
            mRadioButton_sheshi.setChecked(true);
        else mRadioButton_huashi.setChecked(true);
        mRadioButton_huashi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    editor.putString("tempertruemode","huashi");
                    editor.commit();
                }

                Log.i("AAA1",String.valueOf(b));
            }
        });

        mRadioButton_sheshi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    editor.putString("tempertruemode","sheshi");
                    editor.commit();
                }

                Log.i("AAA2",String.valueOf(b));
            }
        });

        return rootview;

    }


}
