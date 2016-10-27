package yzw.weather.model;

/**
 * Created by yangzhiwei on 2016/7/23.
 */
public class Sys {
    public int type;
    public int id;
    public float message;
    public String country;
    public long sunrise;
    public long sunset;

    @Override
    public String toString() {
        return "Sys{" +
                "type=" + type +
                ", id=" + id +
                ", message=" + message +
                ", country='" + country + '\'' +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}