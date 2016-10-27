package yzw.weather.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import yzw.weather.R;
import yzw.weather.model.Oneday;
import yzw.weather.seven.ListObject;
import yzw.weather.seven.Sevenday;
import yzw.weather.util.Utils;

/**
 * Created by yangzhiwei on 2016/8/22.
 */
public class PredictionFragment extends Fragment {
    private static final String TAG = "YZW";
    private static final String EXTRA_CITY_ID = "city_id";
    private static final String EXTRA_CITY_NAME = "city_name";
    private long mCityId;
    private String mCityName;
    private Call callWeather;
    private Call mCallWeather;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static Fragment newInstance(long id, String name) {

        Fragment fragment = new PredictionFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_CITY_ID, id);
        args.putString(EXTRA_CITY_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int i = getArguments().getInt("item_position");
        View rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        preferences = getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
        editor = preferences.edit();
        final predictioncallback mpredictioncallback = (predictioncallback)getActivity();

        final TextView mTextView_up = (TextView) rootView.findViewById(R.id.one_tempertrue_up);
        final TextView mTextView_down = (TextView) rootView.findViewById(R.id.one_tempertrue_down);


        mTextView_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("OUT",preferences.getString("tempertruemode", null));
                if (preferences.getString("tempertruemode", null).equals("sheshi") ) {
                    editor.putString("tempertruemode", "huashi");
                    Log.i("IN1",preferences.getString("tempertruemode", null));
                    mTextView_up.setText("F");
                    mTextView_down.setText("C");
                } else {
                    editor.putString("tempertruemode", "sheshi");
                    Log.i("IN2",preferences.getString("tempertruemode", null));
                    mTextView_up.setText("C");
                    mTextView_down.setText("F");
                }
                editor.commit();
                Log.i("Result",preferences.getString("tempertruemode", null));
                mpredictioncallback.predictioncallback();

            }
        });

        receive();
        okHttp_synchronousGet();
        okHttp_synchronousGet2();
        return rootView;

    }


    private void receive() {
        mCityId = getArguments().getLong(EXTRA_CITY_ID);
        mCityName = getArguments().getString(EXTRA_CITY_NAME);

    }


    private void okHttp_synchronousGet() {

        final String onedayurl = "http://api.openweathermap.org/data/2.5/weather?id=" + mCityId + "&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&units=metric&lang=zh_cn";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(onedayurl).build();
        callWeather = client.newCall(request);
        callWeather.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String zh = response.body().string();
                    Log.i(TAG, onedayurl);
                    Log.i(TAG, zh);
                    Gson gson = new Gson();
                    final Oneday oneday = gson.fromJson(zh, Oneday.class);
                    try {
                        getView().post(new Runnable() {
                            @Override
                            public void run() {
                                output(oneday);
                            }
                        });
                    } catch (Exception ex) {

                    }

//                                getView().post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        output(oneday);
//                                    }
//                                });

                } else {
                    Log.i(TAG, "okHttp is request error");
                }

            }
        });


    }


    private void okHttp_synchronousGet2() {

        final String sevendayurl = "http://api.openweathermap.org/data/2.5/forecast/daily?APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&id=" + mCityId + "&lang=zh_cn&units=metric&cnt=";
        final String[] zh = {""};
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(sevendayurl).build();
        mCallWeather = client.newCall(request);

        mCallWeather.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    zh[0] = response.body().string();
                    Log.i(TAG, zh[0]);
                    Gson gson = new Gson();
                    final Sevenday sevenday = gson.fromJson(zh[0], Sevenday.class);
                    try {
                        getView().post(new Runnable() {
                            @Override
                            public void run() {
                                dealForcast(sevenday);
                            }
                        });
                    } catch (Exception ex) {

                    }


//                    getView().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            dealForcast(sevenday);
//                        }
//                    });
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            dealForcast(sevenday);
//                        }
//                    });


                } else {
                    Log.i(TAG, "okHttp is request error");
                }

            }
        });


    }


    private void output(final Oneday oneday) {
        View rootView = getView();

        final TextView textView = (TextView) rootView.findViewById(R.id.one_textView);
        final TextView textView2 = (TextView) rootView.findViewById(R.id.one_textView2);
        final TextView textView3 = (TextView) rootView.findViewById(R.id.one_textView3);
        final TextView textView5 = (TextView) rootView.findViewById(R.id.one_textView5);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.one_imageView);
        final TextView textView10 = (TextView) rootView.findViewById(R.id.one_textView10);

        textView.setText(mCityName);
        if (preferences.getString("tempertruemode", "sheshi").equals("sheshi"))
            textView2.setText((int) oneday.main.temp + "°");
        else textView2.setText((((int) oneday.main.temp) * 9 / 5 + 32) + "°");
        textView3.setText(String.valueOf(oneday.weather.get(0).description));
        textView5.setText("风速  " + oneday.wind.getDegDesc() + getString(R.string.wind_speed, (int) oneday.wind.speed) + "    湿度  " + String.valueOf(oneday.main.humidity) + "%");

//        textView6.setText("风速");
//        textView7.setText(getString(R.string.wind_speed, (int) oneday.wind.speed));
//        textView8.setText("湿度");
//        textView9.setText(String.valueOf(oneday.main.humidity) + "%");
        imageView.setImageResource(Utils.getIconResId(oneday.weather.get(0).icon));
        textView10.setText(String.valueOf(gettime((long) oneday.dt * 1000l)));
    }

    private void output2(final View child, final ListObject item) {
        TextView seven_textView5 = (TextView) child.findViewById(R.id.seven_textView5);
        TextView seven_textView6 = (TextView) child.findViewById(R.id.seven_textView6);
        TextView seven_textView3 = (TextView) child.findViewById(R.id.seven_textView3);
        TextView seven_textView4 = (TextView) child.findViewById(R.id.seven_textView4);
        ImageView seven_imageview = (ImageView) child.findViewById(R.id.seven_itageView);


        seven_textView3.setText(item.weather.get(0).description);
        seven_imageview.setImageResource(Utils.getIconResId(item.weather.get(0).icon));
        seven_textView4.setText(String.valueOf(getdate((long) item.dt * 1000l)));
        if (preferences.getString("tempertruemode", "sheshi").equals("sheshi")){
            seven_textView5.setText(String.valueOf((int) item.temp.max) + "°");
            seven_textView6.setText(String.valueOf((int) item.temp.min) + "°");
        }
        else{
            seven_textView5.setText(String.valueOf(   (((int) item.temp.max)*9/5+32)      ) + "°");
            seven_textView6.setText(String.valueOf(   (((int) item.temp.min)*9/5+32)     ) + "°");
        }


    }

    private String getdate(long dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dt);
        SimpleDateFormat sdf = new SimpleDateFormat("d日");
        SimpleDateFormat sdf2 = new SimpleDateFormat("E");
        String str;
        String strday = sdf.format(calendar.getTime());
        String strxingqi = sdf2.format(calendar.getTime());
        str = strday + "(" + Oneday.xingqi(strxingqi) + ")";
        return str;
    }

    private String gettime(long dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dt);
        SimpleDateFormat sdf = new SimpleDateFormat("最后更新时间 HH:mm");
        String str = sdf.format(calendar.getTime());
        Log.i("FFF", str);
        return str;
    }

    private void dealForcast(Sevenday sevenday) {
        View rootView = getView();
        LinearLayout scrollContentLayout = (LinearLayout) rootView.findViewById(R.id.scroll_content);
        scrollContentLayout.removeAllViews();
        for (int i = 0; i < sevenday.list.size(); i++) {
            final View child = LayoutInflater.from(getContext()).inflate(R.layout.sevenday, scrollContentLayout, false);
            if (i == 0)
                child.setBackgroundResource(R.drawable.boder);
            output2(child, sevenday.list.get(i));
            scrollContentLayout.addView(child);
        }
    }

    public interface predictioncallback{
        void predictioncallback();
    }
}


