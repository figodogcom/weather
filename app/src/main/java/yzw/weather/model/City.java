package yzw.weather.model;

import android.provider.BaseColumns;

/**
 * Created by yangzhiwei on 2016/9/21.
 */
public final class City/* implements BaseColumns*/ {

    public static final String COLUMN_NAME = "name";

    public static final int COLUMN_COUNT = 5;

    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_NAME = 1;



    public int _id;
    public String name;
    public String country;
    public Coord coord;

    @Override
    public String toString() {
        return "Favourite{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", coord=" + coord +
                '}';
    }
}
