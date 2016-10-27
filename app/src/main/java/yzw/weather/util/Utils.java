package yzw.weather.util;

import yzw.weather.R;
import yzw.weather.model.WeatherObject;

/**
 * Created by yangzhiwei on 2016/7/28.
 */
public class Utils {
    public static int getIconResId(String icon) {
        switch (icon) {
            case "01d":return R.drawable.d01;
            case "02d":return R.drawable.d02;
            case "03d":return R.drawable.d03;
            case "04d":return R.drawable.d04;
            case "09d":return R.drawable.d09;
            case "10d":return R.drawable.d10;
            case "11d":return R.drawable.d11;
            case "13d":return R.drawable.d13;
            case "50d":return R.drawable.d50;
            case "01n":return R.drawable.n01;
            case "02n":return R.drawable.n02;
            case "03n":return R.drawable.n03;
            case "04n":return R.drawable.n04;
            case "09n":return R.drawable.n09;
            case "10n":return R.drawable.n10;
            case "11n":return R.drawable.n11;
            case "13n":return R.drawable.n13;
            case "50n":return R.drawable.n50;

        }
        return R.drawable.d01;
    }
}
