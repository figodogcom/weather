package yzw.weather.model;

/**
 * Created by yangzhiwei on 2016/7/25.
 */
public class WeatherObject {
    public int id;
    public String main;
    public String description;
    public String icon;


    @Override
    public String toString() {
        return "WeatherObject{" +
                "id=" + id +
                ", toggle_item='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
