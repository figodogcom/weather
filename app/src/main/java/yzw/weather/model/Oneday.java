package yzw.weather.model;

import java.util.List;

/**
 * Created by yangzhiwei on 2016/7/25.
 */
public class Oneday {
    public static final String COLUMN_NAME = "name";

    public static final int COLUMN_COUNT = 9;

    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_WEATHER_DESCRIPTION = 1;
    public static final int COLUMN_INDEX_MAIN_TEMP = 2;
    public static final int COLUMN_INDEX_MAIN_TEMP_MIN = 3;
    public static final int COLUMN_INDEX_MAIN_TEMP_MAX = 4;
    public static final int COLUMN_INDEX_WIND_DEG = 5;
    public static final int COLUMN_INDEX_WIND_SPEED = 6;
    public static final int COLUMN_INDEX_MAIN_HUMIDITY = 7;
    public static final int COLUMN_INDEX_WIND_GUST = 8;

    public Coord coord;
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public Sys sys;

    public long dt;
    public String base;
    public int id;
    public String name;
    public int cod;

    public List<WeatherObject> weather;

    public static String xingqi(String str){
        String result = "";
        switch (str){
            case "周一":
                result = "星期一";break;
            case "周二":
                result = "星期二";break;
            case "周三":
                result = "星期三";break;
            case "周四":
                result = "星期四";break;
            case "周五":
                result = "星期五";break;
            case "周六":
                result = "星期六";break;
            case "周日":
                result = "星期日";break;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Oneday{" +
                "coord=" + coord +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", sys=" + sys +
                ", dt=" + dt +
                ", base='" + base + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                ", weather=" + weather +
                '}';
    }
}
