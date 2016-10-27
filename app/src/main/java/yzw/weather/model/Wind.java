package yzw.weather.model;

import android.util.Log;

/**
 * Created by yangzhiwei on 2016/7/23.
 */
public class Wind {
    public double speed;
    public double deg;
    public double gust;

    public String getDegDesc() {
        if (deg <= 22.5 || deg >= 337.5) {
            return "北风";
        } else if (deg <= 67.5) return "东北风";
        else if (deg <= 112.5) return "东风";
        else if (deg <= 157.5) return "东南风";
        else if (deg <= 202.5) return "南风";
        else if (deg <= 247.5) return "西南风";
        else if (deg <= 292.5) return "西风";
        else if (deg <= 337.5) return "西北风";

        return "未知";


    }



}
