package yzw.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yzw.weather.model.City;

/**
 * Created by yangzhiwei on 2016/9/22.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    String sql = "create table favourite(_id integer primary key autoincrement,"
            + City.COLUMN_NAME + " TEXT,"
            + " country varchar(50)," +
            " lon double," +
            " lat double)";

    String sql_weather = "create table weather("
            + "_id integer primary key autoincrement,"
            + "weather_description TEXT,"
            + "main_temp FLOAT,"
            + "main_temp_min FLOAT,"
            + "main_temp_max FLOAT,"
            + "wind_deg INTEGER,"
            + "wind_speed FLOAT,"
            + "main_humidity INTEGER,"
            + "wind_gust FLOAT"
            +")";

    String sql_start = "create table start(_id integer primary key autoincrement,"
            + City.COLUMN_NAME + " TEXT,"
            + " country varchar(50),"
            + " lon double,"
            + " lat double)";

    String sql_guangzhou = "insert into start VALUES(1809858,\"广州\",\"CN\"，110.2，110.1)";



    String sql_drop = "drop table weather";


    public MyDatabaseHelper(Context context) {
        super(context, "weather.db3", null, 7);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        db.execSQL(sql_weather);
        db.execSQL(sql_start);
//        db.execSQL(sql_guangzhou);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL(sql_drop);
        sqLiteDatabase.execSQL(sql_weather);


    }
}
