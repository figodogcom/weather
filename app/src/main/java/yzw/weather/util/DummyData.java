package yzw.weather.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangzhiwei on 2016/8/27.
 */
public class DummyData {

    public static Map<Integer, String> getCityList() {
        Map<Integer, String> city = new HashMap<>();

        city.put(1809858,"广东省广州市");
        city.put(1790437,"广东省珠海市");
        city.put(1795565,"广东省深圳市");
        city.put(1784320,"广东省中山市");
        city.put(1806299,"广东省江门市");
        city.put(1784990,"广东省湛江市");
        city.put(1815395,"广东省潮州市");


        return city;
    }
}
