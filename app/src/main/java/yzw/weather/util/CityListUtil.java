package yzw.weather.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import yzw.weather.model.City;

/**
 * Created by yangzhiwei on 2016/10/9.
 */
public class CityListUtil {

    public static List<yzw.weather.model.City> getCityList() {
        List<yzw.weather.model.City> list = new ArrayList<>();

        return list;
    }


    /**
     *
     * @param key 关键字 如："海"
     * @return
     */
    public static List<yzw.weather.model.City> searchCity(Context context, String key) {
        List<yzw.weather.model.City> citylist = new ArrayList<>();
        AssetManager js = context.getAssets();
        try {

            InputStream json = js.open("city.list.json");
            InputStreamReader reader = new InputStreamReader(json);
            BufferedReader br = new BufferedReader(reader);
            String line = null;

            while ((line = br.readLine()) != null) {
                Gson gson = new Gson();
                City city = gson.fromJson(line, City.class);
                if(city.name.indexOf(key)!=-1 && key != "")
                    citylist.add(city);






            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return citylist;
    }
}
