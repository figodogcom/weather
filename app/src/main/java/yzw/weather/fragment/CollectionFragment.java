package yzw.weather.fragment;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import yzw.weather.CollectionAdapter;
import yzw.weather.db.MyDatabaseHelper;
import yzw.weather.R;
import yzw.weather.model.City;
import yzw.weather.model.Oneday;
import yzw.weather.ncity.Ncity;

/**
 * Created by yangzhiwei on 2016/8/23.
 */
public class CollectionFragment extends Fragment implements CollectionAdapter.CollectionAdapterCallback {

    private static final String TAG = "tag";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    protected CollectionAdapter mAdapter;
    private Call mCallWeather;
    private CollectionFragmentCallback mCollectionCallbacks;
    private SQLiteDatabase db;
    private MyDatabaseHelper dbHelper;
    private CardView cardView_add;
    private Cursor cursor2;
    private Cursor cursor;
    private Cursor cursor3;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    public static Fragment newInstance() {
        Fragment fragment = new CollectionFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection,
                container, false);


        preferences = getContext().getSharedPreferences("weather", Context.MODE_PRIVATE);
        editor = preferences.edit();


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.collection_item);


//        mCityMap = DummyData.getCityList();

        dbHelper = new MyDatabaseHelper(getContext());           /////??????????????
        db = dbHelper.getReadableDatabase();
//        loadfavouriteid();
        setstartcity();
        loadstartData();


        cursor = db.rawQuery("select * from favourite", null);
        cursor2 = db.rawQuery("select * from favourite left join weather on favourite._id = weather._id", null);
        cursor3 = db.rawQuery("select * from start left join weather on start._id = weather._id", null);

        mAdapter = new CollectionAdapter(this, cursor2, cursor3, getContext());
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        loadData();


        return rootView;
    }

    public void onResume() {
        super.onResume();
        Log.i("DDD", "DDD");
        cursor = db.rawQuery("select * from favourite", null);
        loadData();

    }

    private void loadData() {
        String nCityId = getNCityId();
        String onedayurl;
        if (preferences.getString("tempertruemode", null).equals("sheshi"))
            onedayurl = "http://api.openweathermap.org/data/2.5/group?id=" + nCityId + "&units=metric&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&lang=zh_cn";
        else
            onedayurl = "http://api.openweathermap.org/data/2.5/group?id=" + nCityId + "&units=imperial&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&lang=zh_cn";
        Log.i("AAAA", onedayurl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(onedayurl).build();
        mCallWeather = client.newCall(request);

        mCallWeather.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

//                response.isSuccessful();

                String zh = response.body().string();
                Gson gson = new Gson();
                final Ncity ncity = gson.fromJson(zh, Ncity.class);

                for (int i = 0; i < ncity.list.size(); i++) {
                    Oneday oneday = ncity.list.get(i);

                    ContentValues cv = new ContentValues();

                    cv.put("_id", oneday.id);
                    cv.put("main_temp", oneday.main.temp);
                    cv.put("main_temp_min", oneday.main.temp_min);
                    cv.put("main_temp_max", oneday.main.temp_max);
                    cv.put("wind_deg", oneday.wind.deg);
                    cv.put("wind_speed", oneday.wind.speed);
                    cv.put("main_humidity", oneday.main.humidity);
                    cv.put("weather_description", oneday.weather.get(0).description);
                    cv.put("wind_gust", oneday.wind.gust);
                    final Cursor cursor = db.rawQuery("select * from weather where _id= " + ncity.list.get(i).id, null);
                    try {
                        if (cursor.getCount() == 0) {
                            db.insert("weather", null, cv);
                        } else
                            db.update("weather", cv, "_id=?", new String[]{String.valueOf(ncity.list.get(i).id)});

                    } finally {
                        cursor.close();
                    }
                }

                final Cursor cursor = db.rawQuery("select * from favourite left join weather on favourite._id = weather._id", null);
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.changeData(cursor);

                    }
                });




            }
        });

    }

    private void loadstartData() {

        long startid = getstartid();
        String onedayurl;
        if (preferences.getString("tempertruemode", null).equals("sheshi"))
            onedayurl = "http://api.openweathermap.org/data/2.5/group?id=" + startid + "&units=metric&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&lang=zh_cn";
        else
            onedayurl = "http://api.openweathermap.org/data/2.5/group?id=" + startid + "&units=imperial&APPID=d4d7d6adac1bfbf1e6c3a78c4580c657&lang=zh_cn";
        Log.i("AAAA", onedayurl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(onedayurl).build();
        mCallWeather = client.newCall(request);

        mCallWeather.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                response.isSuccessful();

                String zh = response.body().string();
                Gson gson = new Gson();
                final Ncity ncity = gson.fromJson(zh, Ncity.class);

                for (int i = 0; i < ncity.list.size(); i++) {
                    Oneday oneday = ncity.list.get(i);

                    ContentValues cv = new ContentValues();

                    cv.put("_id", oneday.id);
                    cv.put("main_temp", oneday.main.temp);
                    cv.put("main_temp_min", oneday.main.temp_min);
                    cv.put("main_temp_max", oneday.main.temp_max);
                    cv.put("wind_deg", oneday.wind.deg);
                    cv.put("wind_speed", oneday.wind.speed);
                    cv.put("main_humidity", oneday.main.humidity);
                    cv.put("weather_description", oneday.weather.get(0).description);
                    cv.put("wind_gust", oneday.wind.gust);
                    final Cursor cursor = db.rawQuery("select * from weather where _id= " + ncity.list.get(i).id, null);
                    try {
                        if (cursor.getCount() == 0) {
                            db.insert("weather", null, cv);
                        } else
                            db.update("weather", cv, "_id=?", new String[]{String.valueOf(ncity.list.get(i).id)});

                    } finally {
                        cursor.close();
                    }
                }




            }
        });

    }
// TODO
    private void setstartcity() {
        ContentValues cv = new ContentValues();
        cv.put("_id", 1809858);
        cv.put("name", "广州");
        cv.put("country", "CN");
        cv.put("lon", 110.1);
        cv.put("lat", 112.2);
        db.insert("start", null, cv);

    }

    private String getNCityId() {
        String ncityid = "";

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int _idColumn = cursor.getColumnIndex("_id");
            String _id = cursor.getString(_idColumn);
            ncityid = ncityid + _id;
            if (!cursor.isLast())   //之前抽风写cursor2
                ncityid = ncityid + ",";
        }


        return ncityid;
    }

    private long getstartid() {
        Long startid;
        Cursor cursor = db.rawQuery("select * from start", null);
        cursor.moveToPosition(0);
        startid = cursor.getLong(0);
        return startid;
    }

    @Override
    public void onCollectionAdapterItemSelected(long cityId, String cityName) {
        mCollectionCallbacks = (CollectionFragmentCallback) getActivity();
        Log.i("YYYYYY", String.valueOf(cityId));
        mCollectionCallbacks.onCollectionFragmentClickCity(cityId, cityName);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the toggle_item; this adds items to the action bar if it is present.
//
//        getActivity().getMenuInflater().inflate(R.menu.toggle_item_fragment, menu);
//        return true;
//    }


//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//
//        menu.findItem(R.id.main_toolbar_shuffle).setVisible(true);
//        super.onPrepareOptionsMenu(menu);
//    }

    public interface CollectionFragmentCallback {
        void onCollectionFragmentClickCity(long cityId, String cityName);
    }
}
