package com.example.weather.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.weather.Cron.User;
import com.example.weather.VO.LocateWeather;
import com.example.weather.VO.WeatherHour;
import com.example.weather.mapper.WeatherMapper;
import com.example.weather.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/12 12:51
 */
@Service
public class WeatherService {
    private Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value(value = "${weather.key}")
    private String KEY = null;

    @Autowired
    private WeatherMapper weatherMapper;

    public Integer newWeather(Long locate, List<WeatherHour> hours) {
        LocateWeather locateWeather = new LocateWeather();
        locateWeather.setWeatherHours(hours);
        locateWeather.HoursToJSON();
        return weatherMapper.newWeather(locate, locateWeather.getWeather());
    }

    public LocateWeather getTodayWeatherByLocate(Long locate) throws IOException {
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.HOUR_OF_DAY) > 12) {//现在是上午还是下午，因为早上晚上都要发一次天气
            c.set(Calendar.HOUR_OF_DAY, 16);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        } else {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        }
        Timestamp time = new Timestamp(c.getTimeInMillis());
        System.out.println(time);
        System.out.println(c.getTimeInMillis());
        LocateWeather locateWeather = weatherMapper.getTodayWeatherByLocate(locate, time);
        if (locateWeather == null) { //如果少了信息，就再去查一遍
            logger.info("数据库中无数据，补偿一次查询");
            String result = HttpUtil.get(String.format("https://devapi.qweather.com/v7/weather/24h?location=%s&key=%s", locate, KEY));
            List<WeatherHour> hours = JSONArray.parseArray(JSONObject.parseObject(result).getString("hourly"), WeatherHour.class);
            newWeather(locate, hours);
            locateWeather = weatherMapper.getTodayWeatherByLocate(locate, time);
        }
        return locateWeather;
    }
}
