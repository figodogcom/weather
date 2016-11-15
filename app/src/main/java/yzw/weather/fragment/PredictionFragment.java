package yzw.weather.fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
    ProgressBar mprogressBar;
    boolean oneday_requset;
    boolean sevenday_request;
    RelativeLayout monedayview;
    RelativeLayout msevendayview;

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
        msevendayview = (RelativeLayout) rootView.findViewById(R.id.sevenday_view);
        monedayview = (RelativeLayout) rootView.findViewById(R.id.oneday_view);
        monedayview.setVisibility(View.INVISIBLE);
        msevendayview.setVisibility(View.INVISIBLE);

        oneday_requset = false;
        sevenday_request = false;
        preferences = getContext().getSharedPreferences("weather", Context.MODE_PRIVATE);
        editor = preferences.edit();
        if (!preferences.contains("tempertruemode"))
            editor.putString("tempertruemode", "sheshi");
        if (!preferences.contains("predictioncity")) {
            editor.putString("predictioncity", "default");
        }
        editor.commit();
        final predictioncallback mpredictioncallback = (predictioncallback) getActivity();
        mpredictioncallback.predictioncallbackchangemenucolor();

        final TextView mTextView_up = (TextView) rootView.findViewById(R.id.one_tempertrue_up);
        final TextView mTextView_down = (TextView) rootView.findViewById(R.id.one_tempertrue_down);
        mprogressBar = (ProgressBar) rootView.findViewById(R.id.prediction_progressbar);
        mprogressBar.setVisibility(View.VISIBLE);


        mTextView_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getString("tempertruemode", "sheshi").equals("sheshi")) {
                    editor.putString("tempertruemode", "huashi");

                } else {
                    editor.putString("tempertruemode", "sheshi");
                }
                editor.commit();
//                mprogressBar.setVisibility(View.VISIBLE);
//                okHttp_synchronousGet();
//                okHttp_synchronousGet2();
                mpredictioncallback.predictioncallbackreflesh();

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
        final String onedayurl;
        final String lon = preferences.getString("lon", "");
        final String lat = preferences.getString("lat", "");
        Log.i("predictioncity", preferences.getString("predictioncity", ""));
        if (preferences.getString("predictioncity", "default").equals("default")||preferences.getString("predictioncity", "default").equals("collection")) {
            if (preferences.getString("temperturemode", "sheshi").equals("sheshi"))
                onedayurl = "http://api.openweathermap.org/data/2.5/weather?id=" + mCityId + "&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&units=metric&lang=zh_cn";
            else
                onedayurl = "http://api.openweathermap.org/data/2.5/weather?id=" + mCityId + "&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&units=imperial&lang=zh_cn";
        }
        else{
            receive_location();
            if (preferences.getString("temperturemode", "sheshi").equals("sheshi")) {
                onedayurl = "http://api.openweathermap.org/data/2.5/weather?id=&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&units=metric&lang=zh_cn" + "&lat=" + lat + "&lon=" + lon;
                Log.i("onedayurl", onedayurl);

            } else {

                onedayurl = "http://api.openweathermap.org/data/2.5/weather?id=&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&units=imperial&lang=zh_cn" + "&lat=" + lat + "&lon=" + lon;
                Log.i("onedayurl", onedayurl);
            }
        }



        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(onedayurl).build();
        callWeather = client.newCall(request);
        callWeather.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("FIAL", "fail");
                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        mprogressBar.setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG);
                        toast.show();
                        RelativeLayout monedayview = (RelativeLayout) getView().findViewById(R.id.oneday_view);
                        monedayview.setVisibility(View.INVISIBLE);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("success", "ok");
                if (response.code() != 200) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            mprogressBar.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG);
                            toast.show();
                            RelativeLayout monedayview = (RelativeLayout) getView().findViewById(R.id.oneday_view);
                            monedayview.setVisibility(View.INVISIBLE);
                        }
                    });
                    return;
                }
                if (response.isSuccessful()) {
                    Log.i("success2", "ok2");
                    String zh = response.body().string();
                    Log.i(TAG, onedayurl);
                    Log.i(TAG, zh);
                    Gson gson = new Gson();
                    final Oneday oneday = gson.fromJson(zh, Oneday.class);

                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            output(oneday);
                        }
                    });

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


                getView().post(new Runnable() {
                    @Override
                    public void run() {
                        mprogressBar.setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG);
                        toast.show();
                        RelativeLayout msevendayview = (RelativeLayout) getView().findViewById(R.id.sevenday_view);
                        msevendayview.setVisibility(View.INVISIBLE);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            mprogressBar.setVisibility(View.INVISIBLE);
                            Toast toast = Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG);
                            toast.show();
                            RelativeLayout msevendayview = (RelativeLayout) getView().findViewById(R.id.sevenday_view);
                            msevendayview.setVisibility(View.INVISIBLE);
                        }
                    });
                    return;
                }
                if (response.isSuccessful()) {
                    zh[0] = response.body().string();
                    Log.i(TAG, zh[0]);
                    Gson gson = new Gson();
                    final Sevenday sevenday = gson.fromJson(zh[0], Sevenday.class);

                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            dealForcast(sevenday);
                        }
                    });


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
        monedayview.setVisibility(View.VISIBLE);
        final TextView textView = (TextView) rootView.findViewById(R.id.one_textView);
        final TextView textView2 = (TextView) rootView.findViewById(R.id.one_textView2);
        final TextView textView3 = (TextView) rootView.findViewById(R.id.one_textView3);
        final TextView textView5 = (TextView) rootView.findViewById(R.id.one_textView5);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.one_imageView);
        final TextView textView10 = (TextView) rootView.findViewById(R.id.one_textView10);
        final TextView tempertrue_up = (TextView) rootView.findViewById(R.id.one_tempertrue_up);
        final TextView tempertrue_down = (TextView) rootView.findViewById(R.id.one_tempertrue_down);

        if (preferences.getString("predictioncity", null).equals("default")||preferences.getString("predictioncity", null).equals("collection"))
            textView.setText(mCityName);
        else textView.setText(oneday.name);


        if (preferences.getString("tempertruemode", "sheshi").equals("sheshi")) {
            textView2.setText((int) oneday.main.temp + "°");
            tempertrue_up.setText("C");
            tempertrue_down.setText("F");
            textView5.setText("风速  " + oneday.wind.getDegDesc() + getString(R.string.wind_level, (int) oneday.wind.speed) + "    湿度  " + String.valueOf(oneday.main.humidity) + "%");
        } else {
            textView2.setText((((int) oneday.main.temp) * 9 / 5 + 32) + "°");
            tempertrue_up.setText("F");
            tempertrue_down.setText("C");
            textView5.setText("风速  " + oneday.wind.getDegDesc() + getString(R.string.wind_speed, (int) oneday.wind.speed) + "    湿度  " + String.valueOf(oneday.main.humidity) + "%");
        }
        textView3.setText(String.valueOf(oneday.weather.get(0).description));

//        textView6.setText("风速");
//        textView7.setText(getString(R.string.wind_speed, (int) oneday.wind.speed));
//        textView8.setText("湿度");
//        textView9.setText(String.valueOf(oneday.main.humidity) + "%");
        imageView.setImageResource(Utils.getIconResId(oneday.weather.get(0).icon));
        textView10.setText(String.valueOf(gettime((long) oneday.dt * 1000l)));
        oneday_requset = true;
        if (oneday_requset && sevenday_request)
            mprogressBar.setVisibility(View.INVISIBLE);

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
        if (preferences.getString("tempertruemode", "sheshi").equals("sheshi")) {
            seven_textView5.setText(String.valueOf((int) item.temp.max) + "°");
            seven_textView6.setText(String.valueOf((int) item.temp.min) + "°");
        } else {
            seven_textView5.setText(String.valueOf((((int) item.temp.max) * 9 / 5 + 32)) + "°");
            seven_textView6.setText(String.valueOf((((int) item.temp.min) * 9 / 5 + 32)) + "°");
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
        msevendayview.setVisibility(View.VISIBLE);
        LinearLayout scrollContentLayout = (LinearLayout) rootView.findViewById(R.id.scroll_content);
        TextView mTextView = (TextView) rootView.findViewById(R.id.one_textView11);
        mTextView.setText("每日");
        scrollContentLayout.removeAllViews();
        for (int i = 0; i < sevenday.list.size(); i++) {
            final View child = LayoutInflater.from(getContext()).inflate(R.layout.sevenday, scrollContentLayout, false);
            if (i == 0)
                child.setBackgroundResource(R.drawable.boder);
            output2(child, sevenday.list.get(i));
            scrollContentLayout.addView(child);
        }
        sevenday_request = true;
        if (oneday_requset && sevenday_request)
            mprogressBar.setVisibility(View.INVISIBLE);
    }

    public interface predictioncallback {
        void predictioncallbackchangemenucolor();

        void predictioncallbackreflesh();
    }

    void receive_location() {
        final LocationManager mlocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mlocationManager.getProviders(true);
        for (int i = 0; i < providers.size(); i++)
            Log.i("PPP", providers.get(i));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        Location mLocation = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (mLocation != null) {
//            Log.i("mLocation.getLatitude()", mLocation.toString());
//            editor.putString("lat", String.valueOf(mLocation.getLatitude()));
//            editor.putString("lon", String.valueOf(mLocation.getLongitude()));
//        }


        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                , 3000, 8, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        editor.putString("lat", String.valueOf(location.getLatitude()));
                        editor.putString("lon", String.valueOf(location.getLongitude()));
                        Log.i("getlon", String.valueOf(location.getLongitude()));
                        editor.putString("predictioncity", "gps");
                        editor.commit();
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mlocationManager.getLastKnownLocation(s);
                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });

    }
}


