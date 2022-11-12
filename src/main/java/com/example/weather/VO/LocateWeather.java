package com.example.weather.VO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/12 12:53
 */
@Data
public class LocateWeather {
    Long id;
    Long locate;
    String weather;
    Timestamp time;
    List<WeatherHour> weatherHours;
    public void HoursToJSON(){
        this.weather = JSON.toJSONString(this.weatherHours);
    }

    public List<WeatherHour> JSONtoWeather(String JSON){
        return JSONArray.parseArray(JSON,WeatherHour.class);
    }
}
