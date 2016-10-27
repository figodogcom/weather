package yzw.weather;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import yzw.weather.db.MyDatabaseHelper;
import yzw.weather.model.City;

/**
 * Created by yangzhiwei on 2016/9/30.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<City> citylist;

    private Context context;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_collection_search_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final City city = citylist.get(position);
        Log.i("1111","holder" + holder);
        Log.i("2222","holder.cityname" + holder.cityname);
        Log.i("3333","city.name" + city.name);

        holder.cityname.setText(city.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertdb(city);


                ((Activity)context).finish();

            }
        });

    }

    private void insertdb(City city) {

        dbHelper = new MyDatabaseHelper(context);
        db = dbHelper.getReadableDatabase();


        AssetManager js = context.getAssets();


        String line = null;


        ContentValues cv = new ContentValues();

        cv.put("_id", city._id);
        cv.put("name", city.name);
        cv.put("country", city.country);
        cv.put("lon", city.coord.lon);
        cv.put("lat", city.coord.lat);


        Cursor cursor = db.rawQuery("select * from favourite where _id= " + city._id, null);
        try {
            if (cursor.getCount() == 0) {
                db.insert("favourite", null, cv);
            } else
                db.update("favourite", cv, "_id=?", new String[]{String.valueOf(city._id)});
        } finally {
            cursor.close();
        }

    }




    public SearchAdapter() {
        
    }

    public SearchAdapter(List<City> citylist) {
        this.citylist = citylist;

    }

    public void changeData(List<City> cityList,Context context) {
        this.citylist = cityList;
        this.context = context;
        Log.i("QQQ", "cityList:" + cityList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView cityname;

        public ViewHolder(View itemView) {
            super(itemView);
            cityname = (TextView) itemView.findViewById(R.id.search_result);



        }


    }


    @Override
    public int getItemCount() {


        return citylist == null ? 0 : citylist.size();
    }
}
