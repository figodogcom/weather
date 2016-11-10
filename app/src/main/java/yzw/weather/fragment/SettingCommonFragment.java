package yzw.weather.fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import yzw.weather.AutoCompleteAdapter;
import yzw.weather.R;

/**
 * Created by yangzhiwei on 2016/10/26.
 */
public class SettingCommonFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_setting_common, container, false);
        RadioButton mRadioButton_huashi = (RadioButton) rootview.findViewById(R.id.fragment_setting_common_huashi);
        RadioButton mRadioButton_sheshi = (RadioButton) rootview.findViewById(R.id.fragment_setting_common_sheshi);
        RadioButton mRadioButton_default = (RadioButton) rootview.findViewById(R.id.fragment_setting_common_default);
        RadioButton mRadioButton_location = (RadioButton) rootview.findViewById(R.id.fragment_setting_common_location);

        SharedPreferences preferences;
        preferences = getContext().getSharedPreferences("weather", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
//        Log.i("EEEE",preferences.getString("tempertruemode", null));
        if (preferences.getString("tempertruemode", "").equals("sheshi"))
            mRadioButton_sheshi.setChecked(true);
        else mRadioButton_huashi.setChecked(true);

        if (preferences.getString("predictioncity", "").equals("default"))
            mRadioButton_default.setChecked(true);
        else
            mRadioButton_location.setChecked(true);

        mRadioButton_huashi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putString("tempertruemode", "huashi");
                    editor.commit();
                }

                Log.i("AAA1", String.valueOf(b));
            }
        });

        mRadioButton_sheshi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putString("tempertruemode", "sheshi");
                    editor.commit();
                }

                Log.i("AAA2", String.valueOf(b));
            }
        });

        mRadioButton_default.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    editor.putString("predictioncity", "default");
                    editor.commit();
            }
        });


        mRadioButton_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("aaaa", String.valueOf(b));

                if (b) {
                    editor.putString("predictioncity", "gps");
                    editor.commit();

                }


            }
        });

        AutoCompleteTextView mAutoCompleteTextView = (AutoCompleteTextView)rootview.findViewById(R.id.fragment_setting_common_autocompletetextview);

        AutoCompleteAdapter mAutoCompleteAdapter = new AutoCompleteAdapter(getContext());

        mAutoCompleteTextView.setAdapter(mAutoCompleteAdapter);



        return rootview;

    }


}
