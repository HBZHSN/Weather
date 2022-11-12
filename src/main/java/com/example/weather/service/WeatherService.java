package com.example.weather.service;

import com.example.weather.VO.LocateWeather;
import com.example.weather.VO.WeatherHour;
import com.example.weather.mapper.WeatherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private WeatherMapper weatherMapper;

    public Integer newWeather(Long locate, List<WeatherHour> hours) {
        LocateWeather locateWeather = new LocateWeather();
        locateWeather.setWeatherHours(hours);
        locateWeather.HoursToJSON();
        return weatherMapper.newWeather(locate, locateWeather.getWeather());
    }

    public LocateWeather getTodayWeatherByLocate(Long locate) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Timestamp time = new Timestamp(c.getTimeInMillis());
        System.out.println(time);
        System.out.println(c.getTimeInMillis());
        return weatherMapper.getTodayWeatherByLocate(locate, time);
    }

}
