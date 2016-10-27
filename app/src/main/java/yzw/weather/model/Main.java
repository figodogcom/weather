package yzw.weather.model;

/**
 * Created by yangzhiwei on 2016/7/23.
 */
public class Main {
    public double temp;
    public double pressure;
    public int humidity;
    public double temp_min;
    public double temp_max;

    @Override
    public String toString() {
        return "Main{" +
                "temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                '}';
    }
}
