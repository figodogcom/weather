package yzw.weather.ncity;

import java.util.List;

import yzw.weather.model.Oneday;


/**
 * Created by yangzhiwei on 2016/8/30.
 */
public class Ncity {
    public int cnt;
    public List<Oneday> list;


    @Override
    public String toString() {
        return "Ncity{" +
                "cnt=" + cnt +
                ", list=" + list +
                '}';
    }
}
